package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.PDFDocument

interface DocumentGenerator {
    fun generate(document: Document): PDFDocument
}