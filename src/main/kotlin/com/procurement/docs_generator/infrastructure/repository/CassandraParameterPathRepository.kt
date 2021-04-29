package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.entity.ParameterPathEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.repository.ParameterPathRepository
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository

@Repository
class CassandraParameterPathRepository(
    private val session: Session,
    private val transform: TransformService
) : ParameterPathRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "document_generator"
        private const val tableName = "parameter_paths"
        private const val columnPmd = "pmd"
        private const val columnProcessInitiator = "processInitiator"
        private const val columnParameter = "parameter"
        private const val columnRecord = "record"
        private const val columnPath = "path"

        private const val loadCQL =
            """SELECT $columnParameter,
                      $columnRecord,
                      $columnPath
                 FROM $KEY_SPACE.$tableName
                WHERE $columnPmd=?
                  AND $columnProcessInitiator=?;
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)

    override fun load(
        pmd: ProcurementMethod,
        processInitiator: String
    ): List<ParameterPathEntity> {

        val query = preparedLoadCQL.bind().also {
            it.setString(columnPmd, pmd.key)
            it.setString(columnProcessInitiator, processInitiator)
        }

        val resultSet = query.executeRead(session)

        return resultSet.map { row ->
            ParameterPathEntity(
                pmd = pmd,
                processInitiator = processInitiator,
                parameter = ParameterPathEntity.Parameter.creator(row.getString(columnParameter)),
                record = RecordName.creator(row.getString(columnRecord)),
                path = row.getString(columnPath)
            )
        }
    }
}