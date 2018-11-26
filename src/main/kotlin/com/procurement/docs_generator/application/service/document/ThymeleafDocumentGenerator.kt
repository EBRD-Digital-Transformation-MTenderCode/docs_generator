package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.application.service.pdf.PDFGenerator
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.exception.template.TemplateEvaluateException
import com.procurement.docs_generator.infrastructure.common.toString
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.context.IContext
import java.nio.charset.Charset

@Service
class ThymeleafDocumentGenerator(
    private val templateEngine: TemplateEngine,
    private val pdfGenerator: PDFGenerator
) : DocumentGenerator {

    companion object {
        private val log: Logger = Slf4jLogger()
    }

    override val format: Template.Format
        get() = Template.Format.HTML

    override val engine: Template.Engine
        get() = Template.Engine.Thymeleaf

    override fun generate(template: Template, context: Map<String, Any>): PDFDocument {
        log.debug { "Processing template." }
        val html = evaluate(
            body = template.body.toString(Charset.defaultCharset()),
            context = Context().apply {
                for ((key, value) in context)
                    this.setVariable(key, value)
            }
        )

        log.debug { "Generate PDF document." }
        val pdfDocument: PDFDocument = pdfGenerator.generate(html)
        log.debug { "The PDF document was generated." }
        return pdfDocument
    }

    private fun evaluate(body: String, context: IContext): String {
        return try {
            templateEngine.process(body, context)
        } catch (exception: Exception) {
            throw TemplateEvaluateException(
                message = "Error of evaluate template.",
                exception = exception
            )
        }
    }
}