package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.entity.ValueEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

interface ValueRepository {
    fun load(
        pmd: ProcurementMethod,
        documentInitiator: String
    ): List<ValueEntity>
}