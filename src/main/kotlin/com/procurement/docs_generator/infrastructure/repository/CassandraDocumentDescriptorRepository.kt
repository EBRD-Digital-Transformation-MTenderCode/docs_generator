package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.BoundStatement
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.command.id.CommandId
import com.procurement.docs_generator.domain.model.command.id.CommandIdDeserializer
import com.procurement.docs_generator.domain.model.command.id.CommandIdSerializer
import com.procurement.docs_generator.domain.model.document.DocumentDescriptor
import com.procurement.docs_generator.domain.model.document.id.DocumentIdDeserializer
import com.procurement.docs_generator.domain.model.document.id.DocumentIdSerializer
import com.procurement.docs_generator.domain.model.document.kind.DocumentKindDeserializer
import com.procurement.docs_generator.domain.model.document.kind.DocumentKindSerializer
import com.procurement.docs_generator.domain.model.language.LanguageDeserializer
import com.procurement.docs_generator.domain.model.language.LanguageSerializer
import com.procurement.docs_generator.domain.repository.DocumentDescriptorRepository
import com.procurement.docs_generator.exception.database.ReadOperationException
import com.procurement.docs_generator.exception.database.SaveOperationException
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service

@Service
class CassandraDocumentDescriptorRepository(
    private val session: Session
) : DocumentDescriptorRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "documents"
        private const val tableName = "descriptors"
        private const val columnCommandId = "command_id"
        private const val columnDocumentId = "document_id"
        private const val columnDocumentKind = "document_kind"
        private const val columnLang = "lang"
        private const val columnDescriptor = "descriptor"

        private const val loadCQL =
            """SELECT $columnCommandId,
                      $columnDocumentId,
                      $columnDocumentKind,
                      $columnLang,
                      $columnDescriptor
                 FROM $KEY_SPACE.$tableName
                WHERE $columnCommandId=?;
            """

        private const val insertCQL = """
               INSERT INTO $KEY_SPACE.$tableName
               (
                $columnCommandId,
                $columnDocumentId,
                $columnDocumentKind,
                $columnLang,
                $columnDescriptor
               )
               VALUES (?,?,?,?,?) IF NOT EXISTS
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)
    private val preparedInsertCQL = session.prepare(insertCQL)

    override fun load(commandId: CommandId): DocumentDescriptor? {
        val id = CommandIdSerializer.serialize(commandId)
        log.debug { "Attempt to load a document descriptor by command id: '$id'." }

        val query = preparedLoadCQL.bind().also {
            it.setString(columnCommandId, id)
        }

        val resultSet = load(query)
        if (!resultSet.wasApplied())
            throw ReadOperationException(message = "An error occurred when loading a record of the document descriptor by command id: '$id' from the database.")

        val row = resultSet.one()
        return if (row != null) {
            log.debug { "Loaded a document descriptor by command id: '$id'." }
            toDocumentDescriptor(row)
        } else {
            log.debug { "Document descriptor by command id: '$id' not found." }
            null
        }
    }

    override fun save(documentDescriptor: DocumentDescriptor): DocumentDescriptor {
        log.debug { "Attempt to save a document descriptor by command id: '${documentDescriptor.commandId}'." }

        val query = preparedInsertCQL.bind().also {
            it.setString(columnCommandId, CommandIdSerializer.serialize(documentDescriptor.commandId))
            it.setString(columnDocumentId, DocumentIdSerializer.serialize(documentDescriptor.documentId))
            it.setString(columnDocumentKind, DocumentKindSerializer.serialize(documentDescriptor.documentKind))
            it.setString(columnLang, LanguageSerializer.serialize(documentDescriptor.lang))
            it.setString(columnDescriptor, documentDescriptor.descriptor)
        }

        val resultSet = save(query)
        return if (resultSet.wasApplied()) {
            log.debug { "Document descriptor by command id: '${documentDescriptor.commandId}' was saved." }
            documentDescriptor
        } else {
            log.debug { "Document descriptor by command id: '${documentDescriptor.commandId}' was not saved." }
            toDocumentDescriptor(resultSet.one())
        }
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

    private fun save(statement: BoundStatement) = try {
        session.execute(statement)
    } catch (ex: Exception) {
        val message = if (ex.message != null)
            "Error writing to the database. ${ex.message}"
        else
            "Error writing to the database."

        throw SaveOperationException(message = message, cause = ex)
    }

    private fun toDocumentDescriptor(row: Row): DocumentDescriptor {
        return DocumentDescriptor(
            commandId = CommandIdDeserializer.deserialize(row.getString(columnCommandId)),
            documentId = DocumentIdDeserializer.deserialize(row.getString(columnDocumentId)),
            documentKind = DocumentKindDeserializer.deserialize(row.getString(columnDocumentKind)),
            lang = LanguageDeserializer.deserialize(row.getString(columnLang)),
            descriptor = row.getString(columnDescriptor)
        )
    }
}