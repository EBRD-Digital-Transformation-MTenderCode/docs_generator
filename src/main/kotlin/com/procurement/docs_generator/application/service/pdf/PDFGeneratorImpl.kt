package com.procurement.docs_generator.application.service.pdf

import com.lowagie.text.pdf.BaseFont
import com.procurement.docs_generator.domain.model.document.PDFDocument
import org.springframework.stereotype.Service
import org.xhtmlrenderer.pdf.ITextRenderer

@Service
class PDFGeneratorImpl : PDFGenerator {
    override fun generate(htmlDocument: String): PDFDocument {
        return PDFDocument().apply {
            val renderer = ITextRenderer()
            val fontResourceURL = javaClass.classLoader.getResource("fonts/times-new-roman.ttf")
            renderer.fontResolver.addFont(fontResourceURL.path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
            renderer.setDocumentFromString(htmlDocument)
            renderer.layout()
            renderer.createPDF(this.outputStream())
        }
    }
}