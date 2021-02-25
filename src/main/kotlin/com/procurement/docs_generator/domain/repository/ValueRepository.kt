package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.entity.ParameterPathEntity
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

interface ValueRepository {
    fun load(
        pmd: ProcurementMethod,
        documentInitiator: String
    ): List<ParameterPathEntity>
}