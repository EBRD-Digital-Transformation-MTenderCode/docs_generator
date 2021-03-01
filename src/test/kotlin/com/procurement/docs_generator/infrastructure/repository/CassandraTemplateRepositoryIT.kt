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
import com.procurement.docs_generator.domain.model.entity.TemplateEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.repository.TemplateRepository
import com.procurement.docs_generator.infrastructure.cassandra.toCassandraTimestamp
import com.procurement.docs_generator.infrastructure.jackson.transform.JacksonTransformService
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import java.time.LocalDateTime

class CassandraTemplateRepositoryIT {
    companion object {
        private val COUNTRY = Country("country")
        private val PMD = ProcurementMethod.CD
        private val DOCUMENT_INITIATOR = "documentInitiator"
        private val LANG = Language("en")
        private val SUB_GROUP = "work"
        private val DATE = LocalDateTime.now()
        private val TYPE_OF_ENGINE = Template.Engine.Thymeleaf
        private val FORMAT = Template.Format.DOCX
        private val TEMPLATE = ByteBuffer.wrap("template".toByteArray())

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
    private var templateRepository: TemplateRepository = CassandraTemplateRepository(session, transform)

    @AfterEach
    fun clean() {
        clearTables()
        clearInvocations(session)
    }

    @Test
    fun load() {
        insert()
        val actual = templateRepository.load(
            pmd = PMD,
            documentInitiator = DOCUMENT_INITIATOR,
            country = COUNTRY,
            lang = LANG,
            subGroup = SUB_GROUP,
            date = DATE
        )
        val expected = expected()

        assertEquals(expected, actual)
    }

    private fun clearTables() {
        session.execute("TRUNCATE document_generator.templates")
    }

    private fun expected() = TemplateEntity(
        pmd = PMD,
        country = COUNTRY,
        documentInitiator = DOCUMENT_INITIATOR,
        lang = LANG,
        subGroup = SUB_GROUP,
        date = DATE,
        typeOfEngine = TYPE_OF_ENGINE,
        format = FORMAT,
        template = TEMPLATE
    )

    private fun insert() {
        val rec = QueryBuilder.insertInto("document_generator", "templates")
            .value("country", COUNTRY.value)
            .value("pmd", PMD.key)
            .value("documentInitiator", DOCUMENT_INITIATOR)
            .value("lang", LANG.value)
            .value("subGroup", SUB_GROUP)
            .value("date", DATE.toCassandraTimestamp())
            .value("typeOfEngine", TYPE_OF_ENGINE.code)
            .value("format", FORMAT.code)
            .value("template", TEMPLATE)

        session.execute(rec)
    }
}
