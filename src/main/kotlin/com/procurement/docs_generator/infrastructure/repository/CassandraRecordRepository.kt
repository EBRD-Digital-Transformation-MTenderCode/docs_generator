package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.RecordEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.domain.repository.RecordRepository
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
        private const val columnProcessInitiator = "processInitiator"
        private const val columnMainProcess = "mainProcess"
        private const val columnRelationships = "relationships"

        private const val loadCQL =
            """SELECT $columnMainProcess,
                      $columnRelationships
                 FROM $KEY_SPACE.$tableName
                WHERE $columnPmd=?
                  AND $columnCountry=?
                  AND $columnProcessInitiator=?;
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)

    override fun load(
        pmd: ProcurementMethod,
        country: Country,
        processInitiator: String
    ): RecordEntity? {

        val query = preparedLoadCQL.bind().also {
            it.setString(columnPmd, pmd.key)
            it.setString(columnCountry, country.value)
            it.setString(columnProcessInitiator, processInitiator)
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return row?.let {
            val relationships = if (row.getString(columnRelationships).isEmpty())
                emptyList()
            else
                transform.deserializeCollection(row.getString(columnRelationships), RelatedProcessType::class.java)

            RecordEntity(
                pmd = pmd,
                country = country,
                processInitiator = processInitiator,
                mainProcess = RecordName.creator(row.getString(columnMainProcess)),
                relationships = relationships
            )
        }
    }
}