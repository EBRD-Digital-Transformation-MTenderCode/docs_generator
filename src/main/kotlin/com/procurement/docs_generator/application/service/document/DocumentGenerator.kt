package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.domain.model.template.Template

interface DocumentGenerator {
    val format: Template.Format
    val engine: Template.Engine

    fun generate(template: Template, context: Map<String, Any>): PDFDocument
}