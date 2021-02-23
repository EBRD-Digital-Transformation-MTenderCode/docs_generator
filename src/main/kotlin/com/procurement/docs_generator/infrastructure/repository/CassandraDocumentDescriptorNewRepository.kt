package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.document.DocumentDescriptorNew
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.repository.DocumentDescriptorNewRepository
import com.procurement.docs_generator.domain.service.JsonDeserializeService
import com.procurement.docs_generator.exception.database.ReadOperationException
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository

@Repository
class CassandraDocumentDescriptorNewRepository(
    private val session: Session,
    private val transform: JsonDeserializeService
) : DocumentDescriptorNewRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "documents"
        private const val tableName = "descriptors_new"
        private const val columnCpid = "cpid"
        private const val columnOcid = "ocid"
        private const val columnDocuments = "documents"
        private const val columnPmd = "pmd"
        private const val columnCountry = "country"
        private const val columnLang = "lang"
        private const val columnInitiator = "initiator"
        private const val columnDescriptor = "descriptor"

        private const val loadCQL =
            """SELECT $columnDescriptor,
                      $columnDocuments
                 FROM $KEY_SPACE.$tableName
                WHERE $columnCpid=?
                  AND $columnOcid=?
                  AND $columnPmd=?
                  AND $columnCountry=?
                  AND $columnLang=?
                  AND $columnInitiator=?;
            """

        private const val insertCQL = """
               INSERT INTO $KEY_SPACE.$tableName
               (
                $columnCpid,
                $columnOcid,
                $columnDocuments
                $columnPmd,
                $columnCountry,
                $columnLang,
                $columnInitiator,
                $columnDescriptor
               )
               VALUES (?,?,?,?,?,?,?,?) IF NOT EXISTS
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)
    private val preparedInsertCQL = session.prepare(insertCQL)

    override fun load(
        cpid: CPID,
        ocid: OCID,
        pmd: ProcurementMethod,
        country: Country,
        lang: Language,
        documentInitiator: String
    ): DocumentDescriptorNew? {
        log.debug { "Attempt to load a document descriptors by cpid '$cpid' and ocid '$ocid'." }

        val query = preparedLoadCQL.bind().also {
            it.setString(columnCpid, cpid.value)
            it.setString(columnOcid, ocid.value)
            it.setString(columnPmd, pmd.key)
            it.setString(columnCountry, country.value)
            it.setString(columnLang, lang.value)
            it.setString(columnInitiator, documentInitiator)
        }

        val resultSet = query.executeRead(session)
        if (!resultSet.wasApplied())
            throw ReadOperationException(message = "An error occurred when loading a record of the document descriptor by cpid '$cpid' and ocid '$ocid' from the database.")

        val row = resultSet.one()
        return if (row != null) {
            log.debug { "Loaded a document descriptor by cpid '$cpid' and ocid '$ocid'." }
            DocumentDescriptorNew(
                cpid = cpid,
                ocid = ocid,
                pmd = pmd,
                country = country,
                lang = lang,
                documentInitiator = documentInitiator,
                descriptor = row.getString(columnDescriptor),
                documents = transform.deserialize(
                    row.getString(columnDocuments),
                    DocumentDescriptorNew.Documents::class.java
                )
            )
        } else {
            log.debug { "Document descriptor by cpid '$cpid' and ocid '$ocid' not found." }
            null
        }
    }

    override fun save(documentDescriptor: DocumentDescriptorNew): Boolean {
        val query = preparedInsertCQL.bind().also {
            it.setString(columnCpid, documentDescriptor.cpid.value)
            it.setString(columnOcid, documentDescriptor.ocid.value)
            it.setString(columnDocuments, transform.serialize(documentDescriptor.documents))
            it.setString(columnPmd, documentDescriptor.pmd.key)
            it.setString(columnCountry, documentDescriptor.country.value)
            it.setString(columnLang, documentDescriptor.lang.value)
            it.setString(columnInitiator, documentDescriptor.documentInitiator)
            it.setString(columnDescriptor, documentDescriptor.descriptor)
        }

        val resultSet = query.executeWrite(session)
        return resultSet.wasApplied()
    }
}