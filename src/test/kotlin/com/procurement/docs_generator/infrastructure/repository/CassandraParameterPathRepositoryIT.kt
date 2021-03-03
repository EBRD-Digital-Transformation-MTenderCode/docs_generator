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
import com.procurement.docs_generator.domain.model.entity.ParameterPathEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.repository.ParameterPathRepository
import com.procurement.docs_generator.infrastructure.jackson.transform.JacksonTransformService
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CassandraParameterPathRepositoryIT {
    companion object {
        private val PMD = ProcurementMethod.CD
        private val DOCUMENT_INITIATOR = "documentInitiator"
        private val PARAMETER = ParameterPathEntity.Parameter.SUBGROUP
        private val RECORD = RecordName.AC
        private val PATH = "tender.mainProcurementCategory"

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
    private var parameterPathRepository: ParameterPathRepository = CassandraParameterPathRepository(session, transform)

    @AfterEach
    fun clean() {
        clearTables()
        clearInvocations(session)
    }

    @Test
    fun load() {
        insert()
        val actual = parameterPathRepository.load(pmd = PMD, documentInitiator = DOCUMENT_INITIATOR)
        val expected = listOf(expected())

        assertEquals(expected, actual)
    }

    private fun clearTables() {
        session.execute("TRUNCATE document_generator.parameter_paths")
    }


    private fun expected() = ParameterPathEntity(
        pmd = PMD,
        documentInitiator = DOCUMENT_INITIATOR,
        parameter = PARAMETER,
        record = RECORD,
        path = PATH
    )

    private fun insert() {
        val rec = QueryBuilder.insertInto("document_generator", "parameter_paths")
            .value("pmd", PMD.key)
            .value("documentInitiator", DOCUMENT_INITIATOR)
            .value("parameter", PARAMETER.key)
            .value("record ", RECORD.key)
            .value("path ", PATH)
        session.execute(rec)
    }
}
