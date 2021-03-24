package com.procurement.docs_generator.infrastructure.service

import com.procurement.docs_generator.adapter.TemplateStore
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.date.toLocalDateTime
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.logger.info
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.dimension.Mb
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.repository.TemplateRepository
import com.procurement.docs_generator.exception.app.AvailableTemplateException
import com.procurement.docs_generator.exception.app.MultipartFileException
import com.procurement.docs_generator.infrastructure.common.toByteBuffer
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.ByteBuffer
import java.time.LocalDate

@Service
class TemplateServiceImpl(
    private val templateStore: TemplateStore,
    private val templateRepository: TemplateRepository
) : TemplateService {
    companion object {
        private val log: Logger = Slf4jLogger()

        private val MaxFileSize = 50.Mb
    }

    override fun add(id: Document.Id,
                     kind: Document.Kind,
                     lang: Language,
                     date: LocalDate,
                     file: MultipartFile,
                     format: Template.Format,
                     engine: Template.Engine) {
        log.info { "Attempt add template (id: '$id', kind: '$kind', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description}')" }
        val bodyTemplate = getBodyTemplate(file)
        templateStore.add(id = id,
                          kind = kind,
                          lang = lang,
                          date = date,
                          format = format,
                          engine = engine,
                          body = bodyTemplate)
        log.info { "Added template (id: '$id', kind: '$kind', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description}')" }
    }

    override fun update(id: Document.Id,
                        kind: Document.Kind,
                        lang: Language,
                        date: LocalDate,
                        file: MultipartFile,
                        format: Template.Format,
                        engine: Template.Engine) {
        log.info { "Attempt update template (id: '$id', kind: '$kind', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description}')" }
        val bodyTemplate = getBodyTemplate(file)
        templateStore.update(id = id,
                             kind = kind,
                             lang = lang,
                             date = date,
                             format = format,
                             engine = engine,
                             body = bodyTemplate)
        log.info { "Updated template (id: '$id', kind: '$kind', lang: '$lang', date: '$date', format: '${format.description}', engine: '${engine.description}')" }
    }

    override fun getAvailableTemplate(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate): Template {
        log.debug { "Attempt get available template (id: '$id', kind: '$kind', lang: '$lang', date: '$date')" }
        return getAvailableTemplateDate(id = id, kind = kind, lang = lang, date = date)
            .also {
                log.info { "Found available template (id: '$id', kind: '$kind', lang: '$lang', date: '$date') on active date: '$it')" }
            }
            .let {
                templateStore.getTemplate(id = id, kind = kind, lang = lang, date = it)
            }.also {
                log.debug { "Loaded available template (id: '$id', kind: '$kind', lang: '$lang', active date: '${it.startDate}')" }
            }
    }

    override fun add(
        country: Country,
        pmd: ProcurementMethod,
        documentInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDate,
        format: Template.Format,
        engine: Template.Engine,
        file: MultipartFile
    ) {
        log.info { "Attempt add template (country: '$country', pmd: '$pmd', documentInitiator: '$documentInitiator', lang: '$lang', subGroup: '${subGroup}, format: '${format.description}', engine: '${engine.description}')" }
        val template = getBodyTemplate(file)
        templateRepository.save(country, pmd, documentInitiator, lang, subGroup, date.toLocalDateTime(), format, engine, template)
        log.info { "Added template (country: '$country', pmd: '$pmd', documentInitiator: '$documentInitiator', lang: '$lang', subGroup: '${subGroup}, format: '${format.description}', engine: '${engine.description}')" }
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