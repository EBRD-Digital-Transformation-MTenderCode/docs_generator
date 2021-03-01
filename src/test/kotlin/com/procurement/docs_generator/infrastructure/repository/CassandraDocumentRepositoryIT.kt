package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Cluster
import com.datastax.driver.core.HostDistance
import com.datastax.driver.core.PlainTextAuthProvider
import com.datastax.driver.core.PoolingOptions
import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.clearInvocations
import com.nhaarman.mockito_kotlin.spy
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.entity.DocumentEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.repository.DocumentRepository
import com.procurement.docs_generator.infrastructure.jackson.transform.JacksonTransformService
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class CassandraDocumentRepositoryIT {
    companion object {
        private val TABLE = "documents"
        private val CP_ID = CPID("cpid")
        private val OC_ID = OCID("ocid")
        private val COUNTRY = Country("country")
        private val PMD = ProcurementMethod.CD
        private val DOCUMENT_INITIATOR = "documentInitiator"
        private val LANG = Language("lang")
        private val OBJECT_ID = UUID.randomUUID().toString()

        private val DOCUMENTS = listOf(DocumentEntity.Document("doc1"), DocumentEntity.Document("doc2"))
        private val DOCUMENTS_JSON = "[{\"id\":\"doc1\"},{\"id\":\"doc2\"}]"

        private val objectMapper = ObjectMapper().apply { configuration() }
        private val transform = JacksonTransformService(objectMapper)

        private var container: CassandraTestContainer = CassandraContainer.container

        private val poolingOptions = PoolingOptions()
            .setMaxConnectionsPerHost(HostDistance.LOCAL, 1)
        private val cluster = Cluster.builder()
            .addContactPoints(container.contractPoint)
            .withPort(container.port)
            .withoutJMXReporting()
            .withPoolingOptions(poolingOptions)
            .withAuthProvider(PlainTextAuthProvider(container.username, container.password))
            .build()
    }

    private var session: Session = spy(cluster.connect())
    private var documentRepository: DocumentRepository = CassandraDocumentRepository(session, transform)

    @AfterEach
    fun clean() {
        clearTables()
        clearInvocations(session)
    }

    @Test
    fun load() {
        insert()
        val actual = documentRepository.load(
            documentInitiator = DOCUMENT_INITIATOR,
            cpid = CP_ID,
            ocid = OC_ID,
            objectId = OBJECT_ID
        )
        val expected = expected()

        assertEquals(expected, actual)
    }

    @Test
    fun save() {
        val expected = expected()
        documentRepository.save(expected)

        val actual = documentRepository.load(
            documentInitiator = expected.documentInitiator,
            cpid = expected.cpid,
            ocid = expected.ocid,
            objectId = OBJECT_ID
        )

        assertEquals(expected, actual)
    }

    private fun clearTables() {
        session.execute("TRUNCATE document_generator.$TABLE")
    }

    private fun expected() = DocumentEntity(
        pmd = PMD,
        country = COUNTRY,
        documentInitiator = DOCUMENT_INITIATOR,
        lang = LANG,
        objectId = OBJECT_ID,
        ocid = OC_ID,
        cpid = CP_ID,
        documents = DOCUMENTS
    )

    private fun insert() {
        val rec = QueryBuilder.insertInto("document_generator", TABLE)
            .value("cpid", CP_ID.value)
            .value("ocid", OC_ID.value)
            .value("pmd", PMD.key)
            .value("country", COUNTRY.value)
            .value("lang", LANG.value)
            .value("documentInitiator", DOCUMENT_INITIATOR)
            .value("documents", DOCUMENTS_JSON)
            .value("objectId ", OBJECT_ID)
        session.execute(rec)
    }
}
