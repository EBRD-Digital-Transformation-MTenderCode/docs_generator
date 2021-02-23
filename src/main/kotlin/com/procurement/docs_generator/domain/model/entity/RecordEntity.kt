package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordReleaseType
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType

class RecordEntity(
    val pmd: ProcurementMethod,
    val country: Country,
    val lang: Language,
    val documentInitiator: String,
    val mainProcess: RecordReleaseType,
    val relationships: Relationships
) {
    class Relationships(values: List<RelatedProcessType>) : List<RelatedProcessType> by values
}
