package com.procurement.docs_generator.application.service.pdf

import com.procurement.docs_generator.domain.model.document.PDFDocument
import org.springframework.stereotype.Service
import org.xhtmlrenderer.pdf.ITextRenderer

@Service
class PDFGeneratorImpl : PDFGenerator {
    override fun generate(htmlDocument: String): PDFDocument {
        return PDFDocument().apply {
            val renderer = ITextRenderer()
            renderer.setDocumentFromString(htmlDocument)
            renderer.layout()
            renderer.createPDF(this.outputStream())
        }
    }
}