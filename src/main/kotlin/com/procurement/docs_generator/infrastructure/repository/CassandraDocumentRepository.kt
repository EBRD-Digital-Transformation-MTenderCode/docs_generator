package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.entity.DocumentEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.repository.DocumentRepository
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository

@Repository
class CassandraDocumentRepository(
    private val session: Session,
    private val transform: TransformService
) : DocumentRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "document_generator"
        private const val tableName = "documents"
        private const val columnCpid = "cpid"
        private const val columnOcid = "ocid"
        private const val columnDocuments = "documents"
        private const val columnPmd = "pmd"
        private const val columnCountry = "country"
        private const val columnLang = "lang"
        private const val columnDocumentInitiator = "documentInitiator"
        private const val columnObjectId = "objectId"


        private const val loadCQL =
            """SELECT $columnDocuments,
                      $columnPmd,
                      $columnCountry,
                      $columnLang
                 FROM $KEY_SPACE.$tableName
                WHERE $columnCpid=?
                  AND $columnOcid=?
                  AND $columnDocumentInitiator=?
                  AND $columnObjectId=?;
            """

        private const val insertCQL = """
               INSERT INTO $KEY_SPACE.$tableName
               (
                $columnCpid,
                $columnOcid,
                $columnDocumentInitiator,
                $columnObjectId,
                $columnPmd,
                $columnCountry,
                $columnLang,
                $columnDocuments                
               )
               VALUES (?,?,?,?,?,?,?,?) IF NOT EXISTS
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)
    private val preparedInsertCQL = session.prepare(insertCQL)


    override fun load(cpid: CPID, ocid: OCID, documentInitiator: String, objectId: String): DocumentEntity? {
        log.debug { "Attempt to load a document descriptors by cpid '$cpid' and ocid '$ocid'." }

        val query = preparedLoadCQL.bind().also {
            it.setString(columnCpid, cpid.value)
            it.setString(columnOcid, ocid.value)
            it.setString(columnDocumentInitiator, documentInitiator)
            it.setString(columnObjectId, objectId)
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return if (row != null) {
            log.debug { "Loaded a document descriptor by cpid '$cpid' and ocid '$ocid'." }
            DocumentEntity(
                cpid = cpid,
                ocid = ocid,
                pmd = ProcurementMethod.creator(row.getString(columnPmd)),
                country = Country(row.getString(columnCountry)),
                lang = Language(row.getString(columnLang)),
                documentInitiator = documentInitiator,
                documents = transform.deserializeCollection(
                    row.getString(columnDocuments),
                    DocumentEntity.Document::class.java
                ),
                objectId = objectId
            )
        } else {
            log.debug { "Document descriptor by cpid '$cpid' and ocid '$ocid' not found." }
            null
        }
    }

    override fun save(documentDescriptor: DocumentEntity): Boolean {
        val query = preparedInsertCQL.bind().apply {
            setString(columnCpid, documentDescriptor.cpid.value)
            setString(columnOcid, documentDescriptor.ocid.value)
            setString(columnDocumentInitiator, documentDescriptor.documentInitiator)
            setString(columnObjectId, documentDescriptor.objectId)
            setString(columnPmd, documentDescriptor.pmd.key)
            setString(columnCountry, documentDescriptor.country.value)
            setString(columnLang, documentDescriptor.lang.value)
            setString(columnDocuments, transform.serialize(documentDescriptor.documents))
        }

        val resultSet = query.executeWrite(session)
        return resultSet.wasApplied()
    }
}