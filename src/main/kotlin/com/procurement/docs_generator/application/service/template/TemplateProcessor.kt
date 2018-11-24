package com.procurement.docs_generator.application.service.template

import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.mode.Mode

interface TemplateProcessor {
    fun processing(document: Document): String

    val mode: Mode
}
