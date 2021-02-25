package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.RecordEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.domain.repository.RecordRepository
import com.procurement.docs_generator.domain.service.TransformService
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository

@Repository
class CassandraRecordRepository(
    private val session: Session,
    private val transform: TransformService
) : RecordRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "document_generator"
        private const val tableName = "records"
        private const val columnPmd = "pmd"
        private const val columnCountry = "country"
        private const val columnDocumentInitiator = "documentInitiator"
        private const val columnMainProcess = "mainProcess"
        private const val columnRelationships = "relationships"

        private const val loadCQL =
            """SELECT $columnMainProcess,
                      $columnRelationships
                 FROM $KEY_SPACE.$tableName
                WHERE $columnPmd=?
                  AND $columnCountry=?
                  AND $columnDocumentInitiator=?;
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)

    override fun load(
        pmd: ProcurementMethod,
        country: Country,
        documentInitiator: String
    ): RecordEntity? {

        val query = preparedLoadCQL.bind().also {
            it.setString(columnPmd, pmd.key)
            it.setString(columnCountry, country.value)
            it.setString(columnDocumentInitiator, documentInitiator)
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return row?.let {
            RecordEntity(
                pmd = pmd,
                country = country,
                documentInitiator = documentInitiator,
                mainProcess = RecordName.creator(row.getString(columnMainProcess)),
                relationships = transform.deserializeCollection(
                    row.getString(columnRelationships), RelatedProcessType::class.java
                )
            )
        }
    }
}