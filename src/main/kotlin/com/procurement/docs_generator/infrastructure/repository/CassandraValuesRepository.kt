package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.entity.ValueEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.repository.ValuesRepository
import com.procurement.docs_generator.domain.service.JsonDeserializeService
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Repository

@Repository
class CassandraValuesRepository(
    private val session: Session,
    private val transform: JsonDeserializeService
) : ValuesRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "documents"
        private const val tableName = "values"
        private const val columnPmd = "pmd"
        private const val columnDocumentInitiator = "documentInitiator"
        private const val columnParameter = "parameter"
        private const val columnRecord = "record"
        private const val columnPath = "path"

        private const val loadCQL =
            """SELECT $columnParameter,
                      $columnRecord,
                      $columnPath
                 FROM $KEY_SPACE.$tableName
                WHERE $columnPmd=?
                  AND $columnDocumentInitiator=?;
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)

    override fun load(
        pmd: ProcurementMethod,
        documentInitiator: String
    ): ValueEntity? {

        val query = preparedLoadCQL.bind().also {
            it.setString(columnPmd, pmd.key)
            it.setString(columnDocumentInitiator, documentInitiator)
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return row?.let {
            ValueEntity(
                pmd = pmd,
                documentInitiator = documentInitiator,
                parameter = ValueEntity.Parameter.creator(row.getString(columnParameter)),
                record = RecordName.creator(row.getString(columnRecord)),
                path = row.getString(columnPath)
            )
        }
    }
}