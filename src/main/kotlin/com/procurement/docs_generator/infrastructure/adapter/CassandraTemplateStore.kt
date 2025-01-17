package com.procurement.docs_generator.infrastructure.adapter

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.procurement.docs_generator.adapter.TemplateStore
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.id.DocumentIdSerializer
import com.procurement.docs_generator.domain.model.document.kind.DocumentKindSerializer
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.language.LanguageSerializer
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.model.template.engine.TemplateEngineDeserializer
import com.procurement.docs_generator.domain.model.template.engine.TemplateEngineSerializer
import com.procurement.docs_generator.domain.model.template.format.TemplateFormatDeserializer
import com.procurement.docs_generator.domain.model.template.format.TemplateFormatSerializer
import com.procurement.docs_generator.exception.database.ReadOperationException
import com.procurement.docs_generator.exception.template.TemplateAlreadyException
import com.procurement.docs_generator.exception.template.TemplateNotFoundException
import com.procurement.docs_generator.exception.template.TemplateUpdateException
import com.procurement.docs_generator.infrastructure.cassandra.toCassandraLocalDate
import com.procurement.docs_generator.infrastructure.cassandra.toLocalDate
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.time.LocalDate

@Service
class CassandraTemplateStore(
    private val session: Session
) : TemplateStore {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "document_generator"
        private const val tableName = "templates_old"
        private const val columnDocumentId = "document_id"
        private const val columnDocumentKind = "document_kind"
        private const val columnLang = "lang"
        private const val columnStartDate = "start_date"
        private const val columnFormat = "format"
        private const val columnEngine = "engine"
        private const val columnBody = "body"

        private const val loadTemplateDatesCQL =
            """SELECT $columnStartDate
                 FROM $KEY_SPACE.$tableName
                WHERE $columnDocumentId=?
                  AND $columnDocumentKind=?
                  AND $columnLang=?;
            """

        private const val loadTemplateCQL =
            """SELECT $columnStartDate,
                      $columnFormat,
                      $columnEngine,
                      $columnBody
                 FROM $KEY_SPACE.$tableName
                WHERE $columnDocumentId=?
                  AND $columnDocumentKind=?
                  AND $columnLang=?
                  AND $columnStartDate=?;
            """

        private const val insertCQL =
            """INSERT INTO $KEY_SPACE.$tableName (
                           $columnDocumentId,
                           $columnDocumentKind,
                           $columnLang,
                           $columnStartDate,
                           $columnFormat,
                           $columnEngine,
                           $columnBody
               )
               VALUES (?,?,?,?,?,?,?) IF NOT EXISTS;"""

        private const val updateCQL =
            """UPDATE $KEY_SPACE.$tableName
                  SET $columnBody=?,
                      $columnFormat=?,
                      $columnEngine=?
                WHERE $columnDocumentId=?
                  AND $columnDocumentKind=?
                  AND $columnLang=?
                  AND $columnStartDate=?;
            """
    }

    private val preparedLoadTemplateDatesCQL = session.prepare(loadTemplateDatesCQL)
    private val preparedLoadTemplateCQL = session.prepare(loadTemplateCQL)
    private val preparedInsertCQL = session.prepare(insertCQL)
    private val preparedUpdateCQL = session.prepare(updateCQL)

    override fun getTemplateDates(id: Document.Id,
                                  kind: Document.Kind,
                                  lang: Language): List<LocalDate> {
        log.debug { "Attempt to load template dates by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}'." }

        val query = preparedLoadTemplateDatesCQL.bind().also {
            it.setString(columnDocumentId, DocumentIdSerializer.serialize(id))
            it.setString(columnDocumentKind, DocumentKindSerializer.serialize(kind))
            it.setString(columnLang, LanguageSerializer.serialize(lang))
        }

        val resultSet = load(query)
        return if (resultSet.wasApplied()) {
            mutableListOf<LocalDate>().apply {
                for (row in resultSet) {
                    add(row.getDate(columnStartDate).toLocalDate())
                }
            }.also {
                log.debug { "Template dates by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}' was loaded - '${it.size}'." }
            }
        } else
            throw TemplateNotFoundException(id = id, kind = kind, lang = lang)
    }

    override fun getTemplate(id: Document.Id,
                             kind: Document.Kind,
                             lang: Language,
                             date: LocalDate): Template {
        log.debug { "Attempt to load template by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date'." }

        val query = preparedLoadTemplateCQL.bind().also {
            it.setString(columnDocumentId, DocumentIdSerializer.serialize(id))
            it.setString(columnDocumentKind, DocumentKindSerializer.serialize(kind))
            it.setString(columnLang, LanguageSerializer.serialize(lang))
            it.setDate(columnStartDate, date.toCassandraLocalDate())
        }

        val resultSet = load(query)
        return if (resultSet.wasApplied()) {
            resultSet.one().let { row ->
                val startDate: LocalDate = row.getDate(columnStartDate).toLocalDate()
                val format = TemplateFormatDeserializer.deserialize(row.getString(columnFormat))
                val engine = TemplateEngineDeserializer.deserialize(row.getString(columnEngine))
                val columnBody: ByteBuffer = row.getBytes(columnBody)

                Template(
                    startDate = startDate,
                    format = format,
                    engine = engine,
                    body = columnBody
                ).also {
                    log.debug { "Template by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date', format: '${format.description}', engine: '${engine.description} is loaded." }
                }
            }
        } else
            throw TemplateNotFoundException(id = id, kind = kind, lang = lang, date = date)
    }

    override fun add(id: Document.Id,
                     kind: Document.Kind,
                     lang: Language,
                     date: LocalDate,
                     format: Template.Format,
                     engine: Template.Engine,
                     body: ByteBuffer) {
        log.debug { "Attempt write template to database (id: '${id.code}', kind: '${kind.code}', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description})." }

        val query = preparedInsertCQL.bind().also {
            it.setString(columnDocumentId, DocumentIdSerializer.serialize(id))
            it.setString(columnDocumentKind, DocumentKindSerializer.serialize(kind))
            it.setString(columnLang, LanguageSerializer.serialize(lang))
            it.setDate(columnStartDate, date.toCassandraLocalDate())
            it.setString(columnFormat, TemplateFormatSerializer.serialize(format))
            it.setString(columnEngine, TemplateEngineSerializer.serialize(engine))
            it.setBytes(columnBody, body)
        }

        val resultSet = save(query)
        if (!resultSet.wasApplied())
            throw TemplateAlreadyException(id, kind, lang, date)

        log.debug { "Template was added (id: '${id.code}', kind: '${kind.code}', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description}')." }
    }

    override fun update(id: Document.Id,
                        kind: Document.Kind,
                        lang: Language,
                        date: LocalDate,
                        format: Template.Format,
                        engine: Template.Engine,
                        body: ByteBuffer) {
        log.debug { "Attempt write template to database (id: '${id.code}', kind: '${kind.code}', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description})." }

        val query = preparedUpdateCQL.bind().also {
            it.setString(columnDocumentId, DocumentIdSerializer.serialize(id))
            it.setString(columnDocumentKind, DocumentKindSerializer.serialize(kind))
            it.setString(columnLang, LanguageSerializer.serialize(lang))
            it.setDate(columnStartDate, date.toCassandraLocalDate())
            it.setString(columnFormat, TemplateFormatSerializer.serialize(format))
            it.setString(columnEngine, TemplateEngineSerializer.serialize(engine))
            it.setBytes(columnBody, body)
        }

        val resultSet = save(query)
        if (!resultSet.wasApplied())
            throw TemplateUpdateException(id, kind, lang, date)

        log.debug { "Template was updated (id: '${id.code}', kind: '${kind.code}', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description})." }
    }

    protected fun load(statement: BoundStatement): ResultSet = try {
        session.execute(statement)
    } catch (ex: Exception) {
        val message = if (ex.message != null)
            "Error read from the database. ${ex.message}"
        else
            "Error read from the database."
        throw ReadOperationException(message = message, cause = ex)
    }

    protected fun save(statement: BoundStatement): ResultSet = try {
        session.execute(statement)
    } catch (ex: Exception) {
        val message = if (ex.message != null)
            "Error write to the database. ${ex.message}"
        else
            "Error write to the database."
        throw ReadOperationException(message = message, cause = ex)
    }
}