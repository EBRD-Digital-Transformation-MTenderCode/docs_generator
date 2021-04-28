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
import com.procurement.docs_generator.domain.model.entity.RecordEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.domain.repository.RecordRepository
import com.procurement.docs_generator.infrastructure.jackson.transform.JacksonTransformService
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CassandraRecordRepositoryIT {
    companion object {
        private val COUNTRY = Country("country")
        private val PMD = ProcurementMethod.CD
        private val DOCUMENT_INITIATOR = "documentInitiator"
        private val MAIN_PROCESS = RecordName.AC
        private val RELATIONSHIPS = listOf(RelatedProcessType.X_EVALUATION, RelatedProcessType.PARENT)
        private val RELATIONSHIPS_JSON = "[\"x_evaluation\", \"parent\"]"

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
    private var recordRepository: RecordRepository = CassandraRecordRepository(session, transform)

    @AfterEach
    fun clean() {
        clearTables()
        clearInvocations(session)
    }

    @Test
    fun load() {
        insert()
        val actual = recordRepository.load(pmd = PMD, processInitiator = DOCUMENT_INITIATOR, country = COUNTRY)
        val expected = expected()

        assertEquals(expected, actual)
    }

    private fun clearTables() {
        session.execute("TRUNCATE document_generator.records")
    }


    private fun expected() = RecordEntity(
        pmd = PMD,
        country = COUNTRY,
        processInitiator = DOCUMENT_INITIATOR,
        relationships = RELATIONSHIPS,
        mainProcess = MAIN_PROCESS
    )

    private fun insert() {
        val rec = QueryBuilder.insertInto("document_generator", "records")
            .value("country", COUNTRY.value)
            .value("pmd", PMD.key)
            .value("documentInitiator", DOCUMENT_INITIATOR)
            .value("mainProcess", MAIN_PROCESS.key)
            .value("relationships ", RELATIONSHIPS_JSON)
        session.execute(rec)
    }
}
