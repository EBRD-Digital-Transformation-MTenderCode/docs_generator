package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import java.nio.ByteBuffer
import java.time.LocalDateTime

data class TemplateEntity(
    val country: Country,
    val pmd: ProcurementMethod,
    val documentInitiator: String,
    val lang: Language,
    val subGroup: String,
    val date: LocalDateTime,
    val typeOfEngine: Template.Engine,
    val format: Template.Format,
    val template: ByteBuffer
)
