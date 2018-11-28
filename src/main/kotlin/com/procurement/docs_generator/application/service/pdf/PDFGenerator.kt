package com.procurement.docs_generator.application.service.pdf

import com.procurement.docs_generator.domain.model.document.PDFDocument

interface PDFGenerator {
    fun generate(htmlDocument: String): PDFDocument
}