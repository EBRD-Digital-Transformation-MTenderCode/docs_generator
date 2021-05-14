package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.entity.TemplateEntity
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import java.nio.ByteBuffer
import java.time.LocalDateTime

interface TemplateRepository {
    fun load(
        country: Country,
        pmd: ProcurementMethod,
        processInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDateTime
    ): TemplateEntity?

    fun loadDates(
        country: Country,
        pmd: ProcurementMethod,
        processInitiator: String,
        lang: Language,
        subGroup: String
    ): List<LocalDateTime>

    fun save(
        country: Country,
        pmd: ProcurementMethod,
        processInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDateTime,
        format: Template.Format,
        engine: Template.Engine,
        template: ByteBuffer
    )
}