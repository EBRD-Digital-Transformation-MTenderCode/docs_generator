package com.procurement.docs_generator.infrastructure.repository

import com.datastax.driver.core.Session
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.RecordEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordReleaseType
import com.procurement.docs_generator.domain.repository.RecordRepository
import com.procurement.docs_generator.domain.service.JsonDeserializeService
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service

@Service
class CassandraRecordRepository(
    private val session: Session,
    private val transform: JsonDeserializeService
) : RecordRepository {
    companion object {
        private val log: Logger = Slf4jLogger()

        private const val KEY_SPACE = "documents"
        private const val tableName = "records"
        private const val columnPmd = "pmd"
        private const val columnCountry = "country"
        private const val columnDocumentInitiator = "documentInitiator"
        private const val columnLang = "lang"
        private const val columnMainProcess = "mainProcess"
        private const val columnRelationships = "relationships"

        private const val loadCQL =
            """SELECT $columnMainProcess,
                      $columnRelationships
                 FROM $KEY_SPACE.$tableName
                WHERE $columnPmd=?
                  AND $columnCountry=?
                  AND $columnLang=?
                  AND $columnDocumentInitiator=?;
            """
    }

    private val preparedLoadCQL = session.prepare(loadCQL)

    override fun load(
        pmd: ProcurementMethod,
        country: Country,
        lang: Language,
        documentInitiator: String
    ): RecordEntity? {

        val query = preparedLoadCQL.bind().also {
            it.setString(columnPmd, pmd.key)
            it.setString(columnCountry, country.value)
            it.setString(columnLang, lang.value)
            it.setString(columnDocumentInitiator, documentInitiator)
        }

        val resultSet = query.executeRead(session)

        val row = resultSet.one()
        return row?.let {
            RecordEntity(
                pmd = ProcurementMethod.creator(row.getString(columnPmd)),
                country = Country(row.getString(columnCountry)),
                lang = Language(row.getString(columnLang)),
                documentInitiator = row.getString(columnDocumentInitiator),
                mainProcess = RecordReleaseType.creator(row.getString(columnMainProcess)),
                relationships = transform.deserialize(
                    row.getString(columnRelationships),
                    RecordEntity.Relationships::class.java
                )
            )
        }
    }
}