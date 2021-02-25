package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType

data class RecordEntity(
    val pmd: ProcurementMethod,
    val country: Country,
    val documentInitiator: String,
    val mainProcess: RecordName,
    val relationships: List<RelatedProcessType> = emptyList()
)
