package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.adapter.PublicPointAdapter
import com.procurement.docs_generator.adapter.UploadDocumentAdapter
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.command.GenerateDocumentCommand
import com.procurement.docs_generator.domain.command.GenerateDocumentResponse
import com.procurement.docs_generator.domain.command.ac.ContractFinalizationCommand
import com.procurement.docs_generator.domain.command.ac.GenerateACDocCommand
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.info
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.date.JsonDateTimeDeserializer
import com.procurement.docs_generator.domain.model.document.AwardContract
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.context.mapper.GoodsContextMapper
import com.procurement.docs_generator.domain.model.document.context.mapper.ServicesContextMapper
import com.procurement.docs_generator.domain.model.document.context.mapper.WorksContextMapper
import com.procurement.docs_generator.domain.model.entity.DocumentDescriptor
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.ocid.OCIDDeserializer
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.entity.Record
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.repository.DocumentDescriptorNewRepository
import com.procurement.docs_generator.domain.repository.DocumentDescriptorRepository
import com.procurement.docs_generator.domain.repository.RecordRepository
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class DocumentServiceImpl(
    private val publicPointAdapter: PublicPointAdapter,
    documentGenerators: List<DocumentGenerator>,
    private val templateService: TemplateService,
    private val documentDescriptorRepository: DocumentDescriptorRepository,
    private val documentDescriptorRepositoryNew: DocumentDescriptorNewRepository,
    private val recordRepository: RecordRepository,
    private val uploadDocumentAdapter: UploadDocumentAdapter
) : DocumentService {
    companion object {
        private val log: Logger = Slf4jLogger()
    }

    private val generators: Map<Template.Format, Map<Template.Engine, DocumentGenerator>>

    init {
        val result = mutableMapOf<Template.Format, MutableMap<Template.Engine, DocumentGenerator>>()
        for (gen in documentGenerators) {
            val byFormat = result[gen.format]
            if (byFormat == null) {
                result[gen.format] = mutableMapOf<Template.Engine, DocumentGenerator>().apply {
                    this[gen.engine] = gen
                }
            } else {
                if (byFormat.containsKey(gen.engine)) {
                    throw IllegalStateException("Duplicate document generator for '${gen.format.description}' & '${gen.engine.description}' (class: '${gen.javaClass.canonicalName}').")
                }
                byFormat[gen.engine] = gen
            }
        }

        generators = result
    }

    override fun processing(command: GenerateACDocCommand): ContractFinalizationCommand.Data {
        log.info { "Processing command with id: '${command.id}'." }
        val view = documentDescriptorRepository.load(command.id)
            ?.let {
                toDataOfContractFinalizationCommand(
                    ocid = command.data.ocid,
                    cpid = command.data.cpid,
                    documentDescriptor = it
                )
            }
        if (view != null) return view

        val document = getDocument(command)
        val template = templateService.getAvailableTemplate(
            id = document.id,
            kind = document.kind,
            lang = document.lang,
            date = document.date
        )

        val uploadDescriptor = getDocumentGenerator(template)
            .generate(template = template, context = document.context)
            .also {
                log.info { "The Document by id: '${document.id}', kind: '${document.kind}', lang: '${document.lang}', date: '${document.date}' was generated based on the template active from the date: '${template.startDate}'." }
            }
            .use { pdfDocument ->
                uploadDocumentAdapter.upload(pdfDocument)
                    .also { descriptor ->
                        log.info { "Document by id: '${document.id}', kind: '${document.kind}', lang: '${document.lang}', date: '${document.date}' was uploaded (file name: '${pdfDocument.fileName}', size: '${pdfDocument.size}', hash: '${pdfDocument.hash}', descriptor: '$descriptor')." }
                    }
            }

        val descriptor = DocumentDescriptor(
            commandId = command.id,
            documentId = document.id,
            documentKind = document.kind,
            lang = document.lang,
            descriptor = uploadDescriptor
        )

        return documentDescriptorRepository.save(descriptor)
            .also {
                log.info { "Descriptor of the document by id: '${document.id}', kind: '${document.kind}', lang: '${document.lang}', date: '${document.date}' was saved in database (descriptor: '$descriptor')." }
            }
            .let {
                toDataOfContractFinalizationCommand(
                    ocid = command.data.ocid,
                    cpid = command.data.cpid,
                    documentDescriptor = it
                )
            }.also {
                log.info { "Command with id: '${command.id}' was processed." }
            }
    }

    private fun getDocumentGenerator(template: Template): DocumentGenerator {
        val byFormat = generators[template.format]
            ?: throw IllegalStateException("Document generator for '${template.format.description}' not found.")
        return byFormat[template.engine]
            ?: throw IllegalStateException("Document generator for '${template.engine.description}' not found.")
    }

    private fun getDocument(command: GenerateACDocCommand): Document {
        val cpid = command.data.cpid
        val ocid = command.data.ocid
        val language = command.data.language

        val (evOCID, acReleases) =
            getACRecord(publicPointAdapter.getACReleasePackage(cpid = cpid, ocid = ocid))

        val kind = Document.Kind.valueOfCode(acReleases.tender.mainProcurementCategory.toUpperCase())

        val evReleasesPackage = publicPointAdapter.getEVReleasePackage(cpid = cpid, ocid = evOCID)
        val publishDate: LocalDate = JsonDateTimeDeserializer.deserialize(evReleasesPackage.publishedDate).toLocalDate()
        val evReleases = evReleasesPackage.releases[0]
        val msReleases = publicPointAdapter.getMSReleasePackage(cpid = cpid).releases[0]

        val context = when (kind) {
            Document.Kind.GOODS ->
                GoodsContextMapper.mapToContext(publishDate, acReleases, evReleases, msReleases)

            Document.Kind.SERVICES ->
                ServicesContextMapper.mapToContext(publishDate, acReleases, evReleases, msReleases)

            Document.Kind.WORKS ->
                WorksContextMapper.mapToContext(publishDate, acReleases, evReleases, msReleases)
        }

        return AwardContract(
            kind = kind,
            date = publishDate,
            lang = language,
            context = context
        )
    }

    private fun getACRecord(releasesPackage: ACReleasesPackage): EvaluationACRecord {
        for (release in releasesPackage.releases) {
            val evRelatedProcess = release.relatedProcesses.firstOrNull {
                it.relationship.contains("x_evaluation") || it.relationship.contains("x_negotiation")
            }

            if (evRelatedProcess != null) {
                return EvaluationACRecord(
                    ocid = OCIDDeserializer.deserialize(evRelatedProcess.identifier),
                    release = release
                )
            }
        }

        throw IllegalStateException("Relationship with value 'x_evaluation' or x_negotiation is not found.")
    }

    private fun toDataOfContractFinalizationCommand(
        cpid: CPID,
        ocid: OCID,
        documentDescriptor: DocumentDescriptor
    ): ContractFinalizationCommand.Data {
        return ContractFinalizationCommand.Data(
            ocid = ocid,
            cpid = cpid,
            documents = listOf(
                ContractFinalizationCommand.Data.Document(
                    id = documentDescriptor.descriptor
                )
            )
        )
    }

    data class EvaluationACRecord(val ocid: OCID, val release: ACReleasesPackage.Release)

    override fun processing(command: GenerateDocumentCommand): GenerateDocumentResponse.Data {
        val data = command.data
        val documentDescriptorStored = documentDescriptorRepositoryNew
            .load(data.cpid, data.ocid, data.pmd, data.country, data.language, data.documentInitiator)

        if (documentDescriptorStored != null)
            return GenerateDocumentResponse.Data(
                cpid = documentDescriptorStored.cpid,
                ocid = documentDescriptorStored.ocid,
                documentInitiator = documentDescriptorStored.documentInitiator,
                documents = documentDescriptorStored.documents.map { document ->
                    GenerateDocumentResponse.Data.Document(id = document.id)
                }
            )
        val mainProcessInfo = recordRepository.load(data.pmd, data.country, data.documentInitiator)
            ?: throw IllegalStateException("Record not found.")
        val mainProcessRelationships = mainProcessInfo.relationships.toSet()
        val mainProcessName = mainProcessInfo.mainProcess

        val mainProcessRecord = publicPointAdapter.getReleasePackage(data.cpid, data.ocid).releases[0]
        val relatedProcessRecords =
            getRelatedProcessesRecords(data.cpid, listOf(mainProcessRecord), mainProcessRelationships)
                .mapKeys { (relationship, _) -> getRecordNameBy(relationship) }

        val allRecords = relatedProcessRecords + (mainProcessName to mainProcessRecord)
    }

    private fun getRelatedProcessesRecords(
        cpid: CPID,
        parentRecords: Collection<Record>,
        relationships: Set<RelatedProcessType>
    ): Map<RelatedProcessType, Record> {
        val relatedProcesses = parentRecords
            .flatMap { it.relatedProcesses }
            .mapNotNull { relatedProcess ->
                relatedProcess.relationship
                    .firstOrNull { relationship -> relationship in relationships }
                    ?.let { relationship -> relationship to relatedProcess }
            }.toMap()

        if (relatedProcesses.isEmpty() && relationships.isNotEmpty())
            throw IllegalStateException("Relationship(s) '${relationships.joinToString()}' not found in any record release.")

        val relatedProcessesRecords = relatedProcesses.mapValues {
            publicPointAdapter.getReleasePackage(cpid = cpid, ocid = OCID(it.value.identifier!!)).releases[0]
        }
        val remainingRelationShips = relationships - relatedProcesses.keys

        if (remainingRelationShips.isNotEmpty())
            return relatedProcessesRecords + getRelatedProcessesRecords(cpid, relatedProcessesRecords.values, remainingRelationShips)

        return  relatedProcessesRecords
    }

    private fun getRecordNameBy(relationship: RelatedProcessType): RecordName =
        when (relationship) {
            RelatedProcessType.PARENT -> RecordName.MS
            RelatedProcessType.X_EVALUATION -> RecordName.EV
            RelatedProcessType.FRAMEWORK,
            RelatedProcessType.PLANNING,
            RelatedProcessType.X_CONTRACTING,
            RelatedProcessType.X_DEMAND,
            RelatedProcessType.X_EXECUTION,
            RelatedProcessType.X_EXPENDITURE_ITEM,
            RelatedProcessType.X_FRAMEWORK,
            RelatedProcessType.X_FUNDING_SOURCE,
            RelatedProcessType.X_NEGOTIATION,
            RelatedProcessType.X_PCR,
            RelatedProcessType.X_PLANNED,
            RelatedProcessType.X_PRESELECTION,
            RelatedProcessType.X_PRE_AWARD_CATALOG_REQUEST,
            RelatedProcessType.X_PRE_QUALIFICATION,
            RelatedProcessType.X_SCOPE -> throw IllegalStateException("Relationship '$relationship' is not allowed.")
        }

}