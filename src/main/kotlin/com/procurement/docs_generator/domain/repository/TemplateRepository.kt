package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.TemplateEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import java.time.LocalDateTime

interface TemplateRepository {
    fun load(
        country: Country,
        pmd: ProcurementMethod,
        documentInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDateTime,
        typeOfEngine: Template.Engine,
        format: Template.Format
    ): TemplateEntity?
}