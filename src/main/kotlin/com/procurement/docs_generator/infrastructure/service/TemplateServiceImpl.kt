package com.procurement.docs_generator.infrastructure.service

import com.procurement.docs_generator.adapter.TemplateStore
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.model.dimension.toMb
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.exception.app.AvailableTemplateException
import com.procurement.docs_generator.exception.app.MultipartFileException
import com.procurement.docs_generator.infrastructure.common.toByteBuffer
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.ByteBuffer
import java.time.LocalDate

@Service
class TemplateServiceImpl(
    private val templateStore: TemplateStore
) : TemplateService {
    companion object {
        private val MaxFileSize = 50.toMb()
    }

    override fun add(id: Document.Id,
                     kind: Document.Kind,
                     lang: Language,
                     date: LocalDate,
                     file: MultipartFile,
                     format: Template.Format,
                     engine: Template.Engine) {
        val bodyTemplate = getBodyTemplate(file)
        templateStore.add(id = id,
                          kind = kind,
                          lang = lang,
                          date = date,
                          format = format,
                          engine = engine,
                          body = bodyTemplate)
    }

    override fun update(id: Document.Id,
                        kind: Document.Kind,
                        lang: Language,
                        date: LocalDate,
                        file: MultipartFile,
                        format: Template.Format,
                        engine: Template.Engine) {
        val bodyTemplate = getBodyTemplate(file)
        templateStore.update(id = id,
                             kind = kind,
                             lang = lang,
                             date = date,
                             format = format,
                             engine = engine,
                             body = bodyTemplate)
    }

    override fun getAvailableTemplate(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate): Template {
        return getAvailableTemplateDate(id = id, kind = kind, lang = lang, date = date).let {
            templateStore.getTemplate(id = id, kind = kind, lang = lang, date = it)
        }
    }

    private fun getAvailableTemplateDate(id: Document.Id,
                                         kind: Document.Kind,
                                         lang: Language,
                                         date: LocalDate): LocalDate {
        val startDates = templateStore.getTemplateDates(id = id, kind = kind, lang = lang)
            .sortedByDescending { it }

        for (startDate in startDates) {
            if (startDate.isBefore(date) || startDate.isEqual(date))
                return startDate
        }

        throw AvailableTemplateException(id = id, kind = kind, lang = lang, date = date)
    }

    private fun getBodyTemplate(file: MultipartFile): ByteBuffer {
        validationFile(file)
        return file.inputStream.toByteBuffer()
    }

    private fun validationFile(file: MultipartFile) {
        if (file.isEmpty)
            throw MultipartFileException("File is empty.")

        if (file.size > MaxFileSize)
            throw MultipartFileException("File is too long (more that $MaxFileSize bytes).")
    }
}