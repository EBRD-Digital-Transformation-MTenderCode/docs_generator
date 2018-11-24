package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.application.service.pdf.PDFGenerator
import com.procurement.docs_generator.application.service.template.TemplateProcessor
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service

@Service
class DocumentGeneratorImpl(
    private val templateProcessor: TemplateProcessor,
    private val pdfGenerator: PDFGenerator
) : DocumentGenerator {

    companion object {
        private val log: Logger = Slf4jLogger()
    }

    override fun generate(document: Document): PDFDocument {
        log.debug { "Generate document: '${document.id.description}'." }
        val processedTemplate: String = processingTemplate(document)

        log.debug { "Generate PDF for document: '${document.id.description}'." }
        val pdfDocument: PDFDocument = pdfGenerator.generate(processedTemplate)
        log.debug { "The PDF document was generated." }
        return pdfDocument
    }

    private fun processingTemplate(document: Document): String {
        log.debug { "Processing template for document: '${document.id.description}'." }
        return templateProcessor.processing(document)
    }
}