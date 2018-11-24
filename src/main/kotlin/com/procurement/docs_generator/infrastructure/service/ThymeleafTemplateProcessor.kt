package com.procurement.docs_generator.infrastructure.service

import com.procurement.docs_generator.adapter.TemplateStore
import com.procurement.docs_generator.application.service.template.TemplateProcessor
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.mode.Mode
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.exception.app.AvailableTemplateException
import com.procurement.docs_generator.exception.template.TemplateEvaluateException
import com.procurement.docs_generator.infrastructure.common.toString
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.IContext
import java.nio.charset.Charset
import java.time.LocalDate

@Service
class ThymeleafTemplateProcessor(
    private val templateEngine: TemplateEngine,
    private val templateStore: TemplateStore
) : TemplateProcessor {

    override val mode: Mode
        get() = Mode.HTML

    override fun processing(document: Document): String {
        val template = getAvailableTemplate(document)
        val bodyTemplate = template.body.toString(Charset.defaultCharset())
        return evaluate(bodyTemplate, document.context)
    }

    private fun getAvailableTemplate(document: Document): Template {
        val date = getAvailableTemplateDate(document)
        return templateStore.getTemplate(id = document.id, kind = document.kind, lang = document.lang, date = date)
    }

    private fun getAvailableTemplateDate(document: Document): LocalDate {
        val startDates = templateStore.getTemplateDates(id = document.id,
                                                        kind = document.kind,
                                                        lang = document.lang)
            .sortedByDescending { it }

        for (startDate in startDates) {
            if (startDate.isBefore(document.date) || startDate.isEqual(document.date))
                return startDate
        }

        throw AvailableTemplateException(document)
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