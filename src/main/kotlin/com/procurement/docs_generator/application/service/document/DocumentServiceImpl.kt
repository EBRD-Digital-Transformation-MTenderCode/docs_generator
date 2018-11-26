package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.adapter.PublicPointAdapter
import com.procurement.docs_generator.adapter.UploadDocumentAdapter
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.command.ac.ContractFinalizationCommand
import com.procurement.docs_generator.domain.command.ac.GenerateACDocCommand
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.date.JsonDateTimeDeserializer
import com.procurement.docs_generator.domain.model.document.AwardContract
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.DocumentDescriptor
import com.procurement.docs_generator.domain.model.document.context.ServicesContext
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.ocid.OCIDDeserializer
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.repository.DocumentDescriptorRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class DocumentServiceImpl(
    private val publicPointAdapter: PublicPointAdapter,
    documentGenerators: List<DocumentGenerator>,
    private val templateService: TemplateService,
    private val documentDescriptorRepository: DocumentDescriptorRepository,
    private val uploadDocumentAdapter: UploadDocumentAdapter
) : DocumentService {
    companion object {
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
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

        val view = documentDescriptorRepository.load(command.id)
            ?.let {
                toDataOfContractFinalizationCommand(ocid = command.data.ocid,
                                                    cpid = command.data.cpid,
                                                    documentDescriptor = it)
            }
        if (view != null) return view

        val document = getDocument(command)
        val template = templateService.getAvailableTemplate(id = document.id,
                                                            kind = document.kind,
                                                            lang = document.lang,
                                                            date = document.date)

        val uploadDescriptor = getDocumentGenerator(template)
            .generate(template = template, context = document.context)
//            .use { pdfDocument ->
//                uploadDocumentAdapter.upload(pdfDocument)
//            }
            .let { "FFFFF" }

        val descriptor = DocumentDescriptor(
            commandId = command.id,
            documentId = document.id,
            documentKind = document.kind,
            lang = document.lang,
            descriptor = uploadDescriptor
        )

        return documentDescriptorRepository.save(descriptor).let {
            toDataOfContractFinalizationCommand(ocid = command.data.ocid,
                                                cpid = command.data.cpid,
                                                documentDescriptor = it)
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
            publicPointAdapter.getACReleasePackage(cpid = cpid, ocid = ocid)
                .let {
                    getACRecord(it)
                }

        val kind = Document.Kind.valueOfCode(acReleases.tender.mainProcurementCategory.toUpperCase())

        val evReleasesPackage = publicPointAdapter.getEVReleasePackage(cpid = cpid, ocid = evOCID)
        val publishDate: LocalDate = JsonDateTimeDeserializer.deserialize(evReleasesPackage.publishedDate).toLocalDate()

        val msReleasesPackage = publicPointAdapter.getMSReleasePackage(cpid = cpid)

        val context = when (kind) {
            Document.Kind.GOODS -> getACGoodsContext(publishDate, acReleases)
            Document.Kind.SERVICES -> {
                getACServicesContext(publishDate,
                                     acReleases,
                                     evReleasesPackage.releases[0],
                                     msReleasesPackage.releases[0])
            }
            Document.Kind.WORKS -> getACWorksContext(publishDate, acReleases)
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
                it.relationship.contains("x_evaluation")
            }

            if (evRelatedProcess != null) {
                return EvaluationACRecord(
                    ocid = OCIDDeserializer.deserialize(evRelatedProcess.identifier),
                    release = release
                )
            }
        }

        throw IllegalStateException("Relationship with value 'x_evaluation' is not found.")
    }

    private fun getACGoodsContext(publishDate: LocalDate, release: ACReleasesPackage.Release): Map<String, Any> {
        TODO()
    }

    private fun convertDate(date: String): String {
        return JsonDateTimeDeserializer.deserialize(date).toLocalDate().format(dateFormatter)
    }

    private fun getACServicesContext(publishDate: LocalDate,
                                     acRelease: ACReleasesPackage.Release,
                                     evRelease: EVReleasesPackage.Release,
                                     msRelease: MSReleasesPackage.Release): Map<String, Any> {
        val partyBuyer = getParty(role = "buyer", parties = acRelease.parties)
        val partySupplier = getParty(role = "supplier", parties = acRelease.parties)

        val ctx = ServicesContext(
            ac = ServicesContext.AC(
                date = convertDate(acRelease.date),
                contract = acRelease.contracts[0].let { contract ->
                    ServicesContext.AC.Contract(
                        id = contract.id,
                        description = contract.description,
                        value = contract.value.amount.toDouble(),
                        endDate = contract.period.endDate,
                        agreedMetrics = getContractAgreedMetrics(acRelease)
                    )
                },
                tender = acRelease.tender.let { tender ->
                    ServicesContext.AC.Tender(
                        procurementMethodDetails = tender.procurementMethodDetails,
                        classification = tender.classification.let { classification ->
                            ServicesContext.AC.Tender.Classification(
                                id = classification.id,
                                description = classification.description
                            )
                        }
                    )
                },
                buyer = partyBuyer.let { party ->
                    ServicesContext.AC.Buyer(
                        address = party.address.let { address ->
                            ServicesContext.AC.Buyer.Address(
                                country = address.addressDetails.country.description,
                                region = address.addressDetails.region.description,
                                locality = address.addressDetails.locality.description,
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode
                            )
                        },
                        identifier = party.identifier.let { identifier ->
                            ServicesContext.AC.Buyer.Identifier(
                                id = identifier.id,
                                legalName = identifier.legalName
                            )
                        },
                        additionalidentifieres = getSchemeAdditionalIdentifiers(scheme = "MD-FISCAL", party = party)
                            .map {
                                ServicesContext.AC.Buyer.AdditionalIdentifier(it)
                            },
                        contactPoint = ServicesContext.AC.Buyer.ContactPoint(
                            telephone = party.contactPoint.telephone
                        ),
                        persones = party.persones?.map { person ->
                            ServicesContext.AC.Buyer.Person(
                                title = person.title,
                                name = person.name,
                                businessFunctions = ServicesContext.AC.Buyer.Person.BusinessFunctions(
                                    jobTitle = person.businessFunctions.first {
                                        it.type == "authority"
                                    }.jobTitle
                                )
                            )
                        } ?: emptyList(),
                        details = party.details?.let { details ->
                            ServicesContext.AC.Buyer.Details(
                                bankAccount = details.bankAccounts[0].let { bankAccount ->
                                    ServicesContext.AC.Buyer.Details.BankAccounts(
                                        accountIdentification = bankAccount.accountIdentification.id,
                                        identifier = bankAccount.identifier.id,
                                        name = bankAccount.bankName,
                                        address = bankAccount.address.let { address ->
                                            ServicesContext.AC.Buyer.Details.BankAccounts.Address(
                                                country = address.addressDetails.country.description,
                                                region = address.addressDetails.region.description,
                                                locality = address.addressDetails.locality.description,
                                                streetAddress = address.streetAddress,
                                                postalCode = address.postalCode
                                            )
                                        }
                                    )
                                },
                                legalForm = ServicesContext.AC.Buyer.Details.LegalForm(
                                    details.legalForm?.description
                                        ?: throw IllegalStateException("Party -> buyer -> details no contains 'legalForm'.")
                                )
                            )
                        } ?: throw IllegalStateException("Party of the buyer no contains 'details'.")

                    )
                },
                supplier = partySupplier.let { party ->
                    ServicesContext.AC.Supplier(
                        address = party.address.let { address ->
                            ServicesContext.AC.Supplier.Address(
                                country = address.addressDetails.country.description,
                                region = address.addressDetails.region.description,
                                locality = address.addressDetails.locality.description,
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode
                            )
                        },
                        identifier = party.identifier.let { identifier ->
                            ServicesContext.AC.Supplier.Identifier(
                                id = identifier.id,
                                legalName = identifier.legalName
                            )
                        },
                        additionalidentifieres = getSchemeAdditionalIdentifiers(scheme = "MD-FISCAL", party = party)
                            .map {
                                ServicesContext.AC.Supplier.AdditionalIdentifier(it)
                            },
                        contactPoint = ServicesContext.AC.Supplier.ContactPoint(
                            telephone = party.contactPoint.telephone
                        ),
                        persones = party.persones?.map { person ->
                            ServicesContext.AC.Supplier.Person(
                                title = person.title,
                                name = person.name,
                                businessFunctions = person.businessFunctions.firstOrNull {
                                    it.type == "authority"
                                }?.let { businessFunction ->
                                    ServicesContext.AC.Supplier.Person.BusinessFunctions(
                                        jobTitle = businessFunction.jobTitle,
                                        documents = businessFunction.documents.firstOrNull {
                                            it.documentType == "regulatoryDocument"
                                        }?.let { document ->
                                            ServicesContext.AC.Supplier.Person.BusinessFunctions.Documents(
                                                title = document.title ?: "N/A"
                                            )
                                        }
                                            ?: throw IllegalStateException("Document in BusinessFunctions by documentType=='regulatoryDocument' not found.")
                                    )
                                }
                                    ?: throw IllegalStateException("BusinessFunctions by type: 'authority' not found.")

                            )
                        } ?: emptyList(),
                        details = party.details?.let { details ->
                            ServicesContext.AC.Supplier.Details(
                                bankAccount = details.bankAccounts[0].let { bankAccount ->
                                    ServicesContext.AC.Supplier.Details.BankAccounts(
                                        accountIdentification = bankAccount.accountIdentification.id,
                                        identifier = bankAccount.identifier.id,
                                        name = bankAccount.bankName,
                                        address = bankAccount.address.let { address ->
                                            ServicesContext.AC.Supplier.Details.BankAccounts.Address(
                                                country = address.addressDetails.country.description,
                                                region = address.addressDetails.region.description,
                                                locality = address.addressDetails.locality.description,
                                                streetAddress = address.streetAddress,
                                                postalCode = address.postalCode
                                            )
                                        }
                                    )
                                },
                                legalForm = ServicesContext.AC.Supplier.Details.LegalForm(
                                    description = details.legalForm?.description
                                        ?: throw IllegalStateException("Party -> supplier -> details no contains 'legalForm'.")
                                )
                            )
                        } ?: throw IllegalStateException("Party of the supplier no contains 'details'.")

                    )
                },
                award = acRelease.awards.firstOrNull { award ->
                    award.relatedLots[0] == acRelease.tender.lots[0].id
                }?.let { award ->
                    ServicesContext.AC.Award(
                        date = award.date,
                        relatedLot = ServicesContext.AC.Award.RelatedLot(id = award.relatedLots[0]),
                        items = award.items.map { item ->
                            ServicesContext.AC.Award.Item(
                                classification = item.classification.let { classification ->
                                    ServicesContext.AC.Award.Item.Classification(
                                        id = classification.id,
                                        description = classification.description
                                    )
                                },
                                description = item.description,
                                unit = item.unit.let { unit ->
                                    ServicesContext.AC.Award.Item.Unit(
                                        name = unit.name,
                                        value = unit.value.let { value ->
                                            ServicesContext.AC.Award.Item.Unit.Value(
                                                amount = value.amount.toDouble(),
                                                amountNet = value.amountNet.toDouble()
                                            )
                                        }
                                    )
                                },
                                planning = acRelease.planning.budget.let { budget ->
                                    ServicesContext.AC.Award.Item.Planning(
                                        budgetAllocation = budget.budgetAllocation.firstOrNull { budgetAllocation ->
                                            budgetAllocation.relatedItem == item.id
                                        }?.let { budgetAllocation ->
                                            ServicesContext.AC.Award.Item.Planning.BudgetAllocation(
                                                period = budgetAllocation.period.let { period ->
                                                    ServicesContext.AC.Award.Item.Planning.BudgetAllocation.Period(
                                                        startDate = convertDate(period.startDate),
                                                        endDate = convertDate(period.endDate)
                                                    )
                                                },
                                                budgetBreakdownID = budgetAllocation.budgetBreakdownID
                                            )
                                        }
                                            ?: throw IllegalStateException("BudgetAllocation by relatedItem: '${item.id}' not found.")

                                    )
                                },
                                quantity = item.quantity.toDouble(),
                                agreedMetrics = getItemAgreedMetrics(awardId = award.id,
                                                                     itemId = item.id,
                                                                     release = acRelease)
                            )
                        }
                    )
                } ?: throw IllegalStateException("Award not found.")
            ),
            ev = ServicesContext.EV(
                publishDate = publishDate.format(dateFormatter),
                tender = evRelease.tender.let { tender ->
                    ServicesContext.EV.Tender(
                        id = tender.id
                    )
                }
            ),
            ms = ServicesContext.MS(
                tender = msRelease.tender.let { tender ->
                    ServicesContext.MS.Tender(
                        id = tender.id,
                        title = tender.title
                    )
                }
            )
        )

        return mutableMapOf<String, Any>().apply {
            this["context"] = ctx
        }
    }

    private fun getACWorksContext(publishDate: LocalDate, release: ACReleasesPackage.Release): Map<String, Any> {
        TODO()
    }

    private fun getContractAgreedMetrics(release: ACReleasesPackage.Release): ServicesContext.AC.Contract.AgreedMetrics {
        val metrics = ContractAgreedMetrics(
            mutableMapOf<String, String>().apply {
                for (metric in release.contracts[0].agreedMetrics) {
                    if (metric.id.startsWith("cc-general")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-general-1-1" -> this["ccGeneral_1_1Measure"] = it.measure.toString()
                                "cc-general-1-2" -> this["ccGeneral_1_2Measure"] = it.measure.toString()
                                "cc-general-1-3" -> this["ccGeneral_1_3Measure"] = it.measure.toString()
                            }
                        }
                    }

                    if (metric.id.startsWith("cc-buyer")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-buyer-1-1" -> this["ccBuyer_1_1Measure"] = it.measure.toString()
                                "cc-buyer-2-1" -> this["ccBuyer_2_1Measure"] = it.measure.toString()
                                "cc-buyer-2-2" -> this["ccBuyer_2_2Measure"] = it.measure.toString()
                            }
                        }
                    }

                    if (metric.id.startsWith("cc-tenderer")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-tenderer-1-1" -> this["ccTenderer_1_1Measure"] = it.measure.toString()
                                "cc-tenderer-1-2" -> this["ccTenderer_1_2Measure"] = it.measure.toString()
                                "cc-tenderer-2-1" -> this["ccTenderer_2_1Measure"] = it.measure.toString()
                                "cc-tenderer-2-2" -> this["ccTenderer_2_2Measure"] = it.measure.toString()
                                "cc-tenderer-2-3" -> this["ccTenderer_2_3Measure"] = it.measure.toString()
                                "cc-tenderer-2-4" -> this["ccTenderer_2_4Measure"] = it.measure.toString()
                                "cc-tenderer-3-1" -> this["ccTenderer_3_1Measure"] = it.measure.toString()
                                "cc-tenderer-3-2" -> this["ccTenderer_3_2Measure"] = it.measure.toString()
                                "cc-tenderer-3-3" -> this["ccTenderer_3_3Measure"] = it.measure.toString()
                            }
                        }
                    }
                }
            }
        )

        return ServicesContext.AC.Contract.AgreedMetrics(
            ccGenerel_1_1Measure = metrics.ccGeneral_1_1Measure,
            ccGenerel_1_2Measure = metrics.ccGeneral_1_2Measure,
            ccGenerel_1_3Measure = metrics.ccGeneral_1_3Measure,

            ccBuyer_1_1Measure = metrics.ccBuyer_1_1Measure,
            ccBuyer_2_1Measure = metrics.ccBuyer_2_1Measure,
            ccBuyer_2_2Measure = metrics.ccBuyer_2_2Measure,

            ccTenderer_1_1Measure = metrics.ccTenderer_1_1Measure,
            ccTenderer_1_2Measure = metrics.ccTenderer_1_2Measure,
            ccTenderer_2_1Measure = metrics.ccTenderer_2_1Measure,
            ccTenderer_2_2Measure = metrics.ccTenderer_2_2Measure,
            ccTenderer_2_3Measure = metrics.ccTenderer_2_3Measure,
            ccTenderer_2_4Measure = metrics.ccTenderer_2_4Measure,
            ccTenderer_3_1Measure = metrics.ccTenderer_3_1Measure,
            ccTenderer_3_2Measure = metrics.ccTenderer_3_2Measure,
            ccTenderer_3_3Measure = metrics.ccTenderer_3_3Measure
        )
    }

    private fun getItemAgreedMetrics(awardId: String,
                                     itemId: String,
                                     release: ACReleasesPackage.Release): ServicesContext.AC.Award.Item.AgreedMetrics {
        val ccSubject = "cc-subject-$awardId-$itemId"
        println(ccSubject)
        for (metric in release.contracts[0].agreedMetrics) {

            if (metric.id == ccSubject) {
                val metrics = ItemAgreedMetrics(
                    mutableMapOf<String, String>().apply {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-subject-1" -> this["ccSubject_1Measure"] = it.measure.toString()
                                "cc-subject-2" -> this["ccSubject_2Measure"] = it.measure.toString()
                                "cc-subject-3" -> this["ccSubject_3Measure"] = it.measure.toString()
                                "cc-subject-4" -> this["ccSubject_4Measure"] = it.measure.toString()
                                "cc-subject-5" -> this["ccSubject_5Measure"] = it.measure.toString()
                            }
                        }
                    }
                )

                return ServicesContext.AC.Award.Item.AgreedMetrics(
                    ccSubject_1Measure = metrics.ccSubject_1Measure,
                    ccSubject_2Measure = metrics.ccSubject_2Measure,
                    ccSubject_3Measure = metrics.ccSubject_3Measure,
                    ccSubject_4Measure = metrics.ccSubject_4Measure,
                    ccSubject_5Measure = metrics.ccSubject_5Measure
                )
            }
        }

        throw IllegalStateException("Contract not contains '$ccSubject'")
    }

    private fun getParty(role: String,
                         parties: List<ACReleasesPackage.Release.Party>): ACReleasesPackage.Release.Party {
        return parties.firstOrNull {
            it.roles.contains(role)
        } ?: throw IllegalStateException("Parties by role: '$role' not found.")
    }

    private fun getSchemeAdditionalIdentifiers(scheme: String,
                                               party: ACReleasesPackage.Release.Party): List<String> {
        return if (party.additionalIdentifiers != null)
            party.additionalIdentifiers.asSequence()
                .filter { it.scheme == scheme }
                .map { identifier ->
                    identifier.scheme

                }
                .toList()
        else emptyList()
    }

    private fun toDataOfContractFinalizationCommand(cpid: CPID,
                                                    ocid: OCID,
                                                    documentDescriptor: DocumentDescriptor): ContractFinalizationCommand.Data {
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

    class ContractAgreedMetrics(props: Map<String, String>) {
        val ccGeneral_1_1Measure: String by props
        val ccGeneral_1_2Measure: String by props
        val ccGeneral_1_3Measure: String by props

        val ccBuyer_1_1Measure: String by props
        val ccBuyer_2_1Measure: String by props
        val ccBuyer_2_2Measure: String by props

        val ccTenderer_1_1Measure: String by props
        val ccTenderer_1_2Measure: String by props
        val ccTenderer_2_1Measure: String by props
        val ccTenderer_2_2Measure: String by props
        val ccTenderer_2_3Measure: String by props
        val ccTenderer_2_4Measure: String by props
        val ccTenderer_3_1Measure: String by props
        val ccTenderer_3_2Measure: String by props
        val ccTenderer_3_3Measure: String by props
    }

    class ItemAgreedMetrics(props: Map<String, String>) {
        val ccSubject_1Measure: String by props
        val ccSubject_2Measure: String by props
        val ccSubject_3Measure: String by props
        val ccSubject_4Measure: String by props
        val ccSubject_5Measure: String by props
    }
}