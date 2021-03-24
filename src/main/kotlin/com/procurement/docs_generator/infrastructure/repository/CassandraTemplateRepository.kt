package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.TemplateEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.repository.TemplateRepository
import com.procurement.docs_generator.infrastructure.cassandra.toCassandraTimestamp
import com.procurement.docs_generator.infrastructure.cassandra.toLocalDateTime
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository
import java.nio.ByteBuffer
import java.time.LocalDateTime

@Repository
class CassandraTemplateRepository(
    private val session: Session,
    private val transform: TransformService
) : TemplateRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "document_generator"
        private const val tableName = "templates"
        private const val columnCountry = "country"
        private const val columnPmd = "pmd"
        private const val columnDocumentInitiator = "documentInitiator"
        private const val columnLang = "lang"
        private const val columnSubGroup = "subGroup"
        private const val columnDate = "date"
        private const val columnTypeOfEngine = "typeOfEngine"
        private const val columnFormat = "format"
        private const val columnTemplate = "template"

        private const val loadCQL =
            """SELECT $columnTemplate,
                      $columnFormat,
                      $columnTypeOfEngine
                 FROM $KEY_SPACE.$tableName
                WHERE $columnCountry=?
                  AND $columnPmd=?
                  AND $columnDocumentInitiator=?
                  AND $columnLang=?
                  AND $columnSubGroup=?
                  AND $columnDate=?;
            """

        private const val loadDatesCQL =
            """SELECT $columnDate
                 FROM $KEY_SPACE.$tableName
                WHERE $columnCountry=?
                  AND $columnPmd=?
                  AND $columnDocumentInitiator=?
                  AND $columnLang=?
                  AND $columnSubGroup=?;
            """

        private const val saveCQL = """
            INSERT INTO $KEY_SPACE.$tableName(
                  $columnCountry,
                  $columnPmd,
                  $columnDocumentInitiator,
                  $columnLang,
                  $columnSubGroup,
                  $columnDate,
                  $columnTypeOfEngine,
                  $columnFormat,
                  $columnTemplate
          ) 
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)
    private val preparedLoadDatesCQL = session.prepare(loadDatesCQL)
    private val preparedSaveCQL = session.prepare(saveCQL)

    override fun load(
        country: Country,
        pmd: ProcurementMethod,
        documentInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDateTime
    ): TemplateEntity? {
        val query = preparedLoadCQL.bind().also {
            it.setString(columnCountry, country.value)
            it.setString(columnPmd, pmd.key)
            it.setString(columnDocumentInitiator, documentInitiator)
            it.setString(columnLang, lang.value)
            it.setString(columnSubGroup, subGroup)
            it.setTimestamp(columnDate, date.toCassandraTimestamp())
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return row?.let {
            TemplateEntity(
                country = country,
                pmd = pmd,
                documentInitiator = documentInitiator,
                lang = lang,
                subGroup = subGroup,
                date = date,
                typeOfEngine = Template.Engine.valueOfCode(row.getString(columnTypeOfEngine)),
                format = Template.Format.valueOfCode(row.getString(columnFormat)),
                template = row.getBytes(columnTemplate)
            )
        }
    }

    override fun loadDates(
        country: Country,
        pmd: ProcurementMethod,
        documentInitiator: String,
        lang: Language,
        subGroup: String
    ): List<LocalDateTime> {
        val query = preparedLoadDatesCQL.bind().also {
            it.setString(columnCountry, country.value)
            it.setString(columnPmd, pmd.key)
            it.setString(columnDocumentInitiator, documentInitiator)
            it.setString(columnLang, lang.value)
            it.setString(columnSubGroup, subGroup)
        }

        return query.executeRead(session)
            .map { row -> row.getTimestamp(columnDate).toLocalDateTime() }
    }

    override fun save(
        country: Country,
        pmd: ProcurementMethod,
        documentInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDateTime,
        format: Template.Format,
        engine: Template.Engine,
        template: ByteBuffer
    ) {
        val query = preparedSaveCQL.bind().also {
            it.setString(columnCountry, country.value)
            it.setString(columnPmd, pmd.key)
            it.setString(columnDocumentInitiator, documentInitiator)
            it.setString(columnLang, lang.value)
            it.setString(columnSubGroup, subGroup)
            it.setTimestamp(columnDate, date.toCassandraTimestamp())
            it.setString(columnFormat, format.code)
            it.setString(columnTypeOfEngine, engine.code)
            it.setBytes(columnTemplate, template)
        }
        query.executeWrite(session)
    }

}