package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.RecordEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

interface RecordRepository {
    fun load(
        pmd: ProcurementMethod,
        country: Country,
        processInitiator: String
    ): RecordEntity?
}