package com.procurement.docs_generator.application.service.document

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.docs_generator.adapter.PublicPointAdapter
import com.procurement.docs_generator.adapter.UploadDocumentAdapter
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.domain.model.release.entity.Record
import com.procurement.docs_generator.domain.model.release.entity.RecordPackage
import com.procurement.docs_generator.domain.model.release.entity.RecordRelatedProcess
import com.procurement.docs_generator.domain.model.release.entity.RelatedProcessScheme
import com.procurement.docs_generator.domain.repository.DocumentDescriptorRepository
import com.procurement.docs_generator.domain.repository.DocumentRepository
import com.procurement.docs_generator.domain.repository.ParameterPathRepository
import com.procurement.docs_generator.domain.repository.RecordRepository
import com.procurement.docs_generator.domain.repository.TemplateRepository
import com.procurement.docs_generator.domain.service.TransformService
import com.procurement.docs_generator.exception.app.GenerateDocumentErrors
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DocumentServiceImplTest {

    companion object{
        private val CP_ID = CPID("cpid")
    }

    private lateinit var publicPointAdapter: PublicPointAdapter
    private lateinit var documentGenerators: List<DocumentGenerator>
    private lateinit var templateService: TemplateService
    private lateinit var documentDescriptorRepository: DocumentDescriptorRepository
    private lateinit var documentRepository: DocumentRepository
    private lateinit var recordRepository: RecordRepository
    private lateinit var parameterPathRepository: ParameterPathRepository
    private lateinit var templateRepository: TemplateRepository
    private lateinit var uploadDocumentAdapter: UploadDocumentAdapter
    private lateinit var transform: TransformService
    private lateinit var documentService: DocumentServiceImpl

    @BeforeEach
    fun init() {
        publicPointAdapter = mock()
        documentGenerators = emptyList()
        templateService = mock()
        documentDescriptorRepository = mock()
        documentRepository = mock()
        recordRepository = mock()
        parameterPathRepository = mock()
        templateRepository = mock()
        uploadDocumentAdapter = mock()
        transform = mock()

        documentService = DocumentServiceImpl(
            publicPointAdapter,
            documentGenerators,
            templateService,
            documentDescriptorRepository,
            documentRepository,
            recordRepository,
            parameterPathRepository,
            templateRepository,
            uploadDocumentAdapter,
            transform
        )
    }

    @Test
    fun getRelatedProcessesRecords_success() {
        val mainProcessRecord = getRecord()
        val evRecord = mainProcessRecord.copy(
            relatedProcesses =  mutableListOf(
                RecordRelatedProcess(
                    id = "",
                    scheme = RelatedProcessScheme.OCID,
                    uri = "",
                    relationship = listOf(RelatedProcessType.X_NEGOTIATION),
                    identifier = "ocid-NP"
                ),
                RecordRelatedProcess(
                    id = "",
                    scheme = RelatedProcessScheme.OCID,
                    uri = "",
                    relationship = listOf(RelatedProcessType.X_SCOPE),
                    identifier = "some-ocid"
                )
            )
        )
        val msRecord = mainProcessRecord.copy(relatedProcesses = mutableListOf())
        val npRecord = mainProcessRecord.copy(relatedProcesses = mutableListOf())

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-EV")))
            .thenReturn(RecordPackage(listOf(evRecord)))

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-MS")))
            .thenReturn(RecordPackage(listOf(msRecord)))

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-NP")))
            .thenReturn(RecordPackage(listOf(npRecord)))

        val allowedRelationships = setOf(RelatedProcessType.X_EVALUATION, RelatedProcessType.X_NEGOTIATION, RelatedProcessType.PARENT)

        val expected: Map<RelatedProcessType, Record> = mapOf(
            RelatedProcessType.X_EVALUATION to evRecord,
            RelatedProcessType.X_NEGOTIATION to npRecord,
            RelatedProcessType.PARENT to msRecord
        )

        val actual = documentService.getRelatedProcessesRecords(cpid = CP_ID, parentRecords = listOf(mainProcessRecord), relationships = allowedRelationships )

        assertEquals(expected, actual)
    }

    @Test
    fun getRelatedProcessesRecords_X_PCRNotFoundInAnyRelatedProcess_fail() {
        val mainProcessRecord = getRecord()
        val evRecord = mainProcessRecord.copy(
            relatedProcesses =  mutableListOf(
                RecordRelatedProcess(
                    id = "",
                    scheme = RelatedProcessScheme.OCID,
                    uri = "",
                    relationship = listOf(RelatedProcessType.X_NEGOTIATION),
                    identifier = "ocid-NP"
                ),
                RecordRelatedProcess(
                    id = "",
                    scheme = RelatedProcessScheme.OCID,
                    uri = "",
                    relationship = listOf(RelatedProcessType.X_SCOPE),
                    identifier = "some-ocid"
                )
            )
        )
        val msRecord = mainProcessRecord.copy(relatedProcesses = mutableListOf())
        val npRecord = mainProcessRecord.copy(relatedProcesses = mutableListOf())

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-EV")))
            .thenReturn(RecordPackage(listOf(evRecord)))

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-MS")))
            .thenReturn(RecordPackage(listOf(msRecord)))

        whenever(publicPointAdapter.getReleasePackage(cpid = CP_ID, ocid = OCID("ocid-NP")))
            .thenReturn(RecordPackage(listOf(npRecord)))

        val allowedRelationships = setOf(RelatedProcessType.X_EVALUATION, RelatedProcessType.X_NEGOTIATION, RelatedProcessType.PARENT, RelatedProcessType.X_PCR)

        val error = assertThrows<GenerateDocumentErrors.RelationshipsNotFound> {
            documentService.getRelatedProcessesRecords(cpid = CP_ID, parentRecords = listOf(mainProcessRecord), relationships = allowedRelationships )
        }
        assertEquals("500.17.00.VR.COM-19.1.1", error.codeError.code)
    }

    @Test
    fun getRelatedProcessesRecords_mainProcessContainsNoRelatedProcesses_fail() {
        val mainProcessRecord = getRecord().copy(relatedProcesses = mutableListOf())

        val allowedRelationships = setOf(RelatedProcessType.X_EVALUATION, RelatedProcessType.X_NEGOTIATION, RelatedProcessType.PARENT)

        val error = assertThrows<GenerateDocumentErrors.RelationshipsNotFound> {
            documentService.getRelatedProcessesRecords(cpid = CP_ID, parentRecords = listOf(mainProcessRecord), relationships = allowedRelationships )
        }
        assertEquals("500.17.00.VR.COM-19.1.1", error.codeError.code)
    }

    private fun getRecord() = Record(
        ocid = "ocid",
        date = null,
        contracts = emptyList(),
        tender = mock(),
        id = null,
        relatedProcesses = mutableListOf(
            RecordRelatedProcess(
                id = "",
                scheme = RelatedProcessScheme.OCID,
                uri = "",
                relationship = listOf(RelatedProcessType.X_EVALUATION),
                identifier = "ocid-EV"
            ),
            RecordRelatedProcess(
                id = "",
                scheme = RelatedProcessScheme.OCID,
                uri = "",
                relationship = listOf(RelatedProcessType.PARENT),
                identifier = "ocid-MS"
            ),
            RecordRelatedProcess(
                id = "",
                scheme = RelatedProcessScheme.OCID,
                uri = "",
                relationship = listOf(RelatedProcessType.X_SCOPE),
                identifier = "some-ocid"
            )
        ),
        agreedMetrics = emptyList(),
        awards = emptyList(),
        bids = null,
        hasPreviousNotice = null,
        initiationType = null,
        invitations = emptyList(),
        parties = mutableListOf(),
        planning = null,
        preQualification = null,
        purposeOfNotice = null,
        qualifications = emptyList(),
        submissions = null,
        tag = emptyList()
    )
}