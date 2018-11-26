package com.procurement.docs_generator.infrastructure.adapter

import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.domain.date.JsonDateDeserializer
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.document.id.DocumentIdDeserializer
import com.procurement.docs_generator.domain.model.document.kind.DocumentKindDeserializer
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.language.LanguageDeserializer
import com.procurement.docs_generator.domain.model.template.Template
import com.procurement.docs_generator.domain.model.template.engine.TemplateEngineDeserializer
import com.procurement.docs_generator.domain.model.template.format.TemplateFormatDeserializer
import com.procurement.docs_generator.domain.view.View
import com.procurement.docs_generator.domain.view.web.AddedTemplateWebView
import com.procurement.docs_generator.domain.view.web.WebErrorView
import com.procurement.docs_generator.exception.app.ApplicationException
import com.procurement.docs_generator.exception.app.InvalidValueOfParamException
import com.procurement.docs_generator.exception.template.TemplateInvalidFormatException
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@RestController
class TemplateController(
    private val templateService: TemplateService
) {
    companion object {
        private val log: Logger = Slf4jLogger()
    }

    @PostMapping(value = ["/templates/{id}/{kind}/{lang}/{date}"],
                 consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun newTemplate(@PathVariable(name = "id") id: String?,
                    @PathVariable(name = "kind") kind: String?,
                    @PathVariable(name = "lang") lang: String?,
                    @PathVariable(name = "date") date: String?,
                    @RequestParam(name = "format", required = false) format: String?,
                    @RequestParam(name = "engine", required = false) engine: String?,
                    @RequestParam(name = "file") file: MultipartFile?): View {

        templateService.add(
            id = getId(id),
            kind = getKind(kind),
            lang = getLang(lang),
            date = getDate(date),
            file = getFile(file),
            format = getFormat(format ?: Template.Format.HTML.code),
            engine = getEngine(engine ?: Template.Engine.Thymeleaf.code)
        )

        return AddedTemplateWebView()
    }

    @PutMapping(value = ["/templates/{id}/{kind}/{lang}/{date}"],
                consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateTemplate(@PathVariable(name = "id") id: String?,
                       @PathVariable(name = "kind") kind: String?,
                       @PathVariable(name = "lang") lang: String?,
                       @PathVariable(name = "date") date: String?,
                       @RequestParam(name = "format", required = false) format: String? = Template.Format.HTML.code,
                       @RequestParam(name = "engine",
                                     required = false) engine: String? = Template.Engine.Thymeleaf.code,
                       @RequestParam(name = "file") file: MultipartFile?): View {

        templateService.update(
            id = getId(id),
            kind = getKind(kind),
            lang = getLang(lang),
            date = getDate(date),
            file = getFile(file),
            format = getFormat(format ?: Template.Format.HTML.code),
            engine = getEngine(engine ?: Template.Engine.Thymeleaf.code)
        )

        return AddedTemplateWebView()
    }

    private fun getId(id: String?): Document.Id {
        return if (id == null || id.isBlank())
            throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'id'", valueParam = "missing")
        else
            try {
                DocumentIdDeserializer.deserialize(id)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'id'", valueParam = id)
            }
    }

    private fun getKind(kind: String?): Document.Kind {
        return if (kind == null || kind.isBlank())
            throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'kind'", valueParam = "missing")
        else
            try {
                DocumentKindDeserializer.deserialize(kind)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'kind'", valueParam = kind)
            }
    }

    private fun getLang(lang: String?): Language {
        return if (lang == null || lang.isEmpty())
            throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'lang'", valueParam = "missing")
        else
            try {
                LanguageDeserializer.deserialize(lang)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'lang'", valueParam = lang)
            }
    }

    private fun getDate(date: String?): LocalDate {
        return if (date == null || date.isBlank())
            throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'date", valueParam = "missing")
        else
            try {
                JsonDateDeserializer.deserialize(date)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "path variable: 'date'", valueParam = date)
            }
    }

    private fun getFile(file: MultipartFile?): MultipartFile {
        return file ?: throw InvalidValueOfParamException(nameAndTypeParam = "request param: 'file'",
                                                          valueParam = "missing")
    }

    private fun getFormat(format: String?): Template.Format {
        return if (format == null || format.isBlank())
            throw InvalidValueOfParamException(nameAndTypeParam = "request param: 'format", valueParam = "missing")
        else
            try {
                TemplateFormatDeserializer.deserialize(format)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "request param: 'format'", valueParam = format)
            }
    }

    private fun getEngine(engine: String?): Template.Engine {
        return if (engine == null || engine.isBlank())
            throw InvalidValueOfParamException(nameAndTypeParam = "request param: 'engine", valueParam = "missing")
        else
            try {
                TemplateEngineDeserializer.deserialize(engine)
            } catch (exception: Exception) {
                throw InvalidValueOfParamException(nameAndTypeParam = "request param: 'engine'", valueParam = engine)
            }
    }

    @ExceptionHandler(value = [ApplicationException::class])
    private fun applicationException(exception: ApplicationException): ResponseEntity<View> {
        val message: String = exception.message!!

        val view: WebErrorView = when (exception) {
            is TemplateInvalidFormatException -> {
                WebErrorView(
                    errors = listOf(
                        WebErrorView.Error(code = exception.codeError.code, description = message)
                    )
                )
            }
            else -> {
                log.error(message)
                WebErrorView(
                    errors = listOf(
                        WebErrorView.Error(
                            code = exception.codeError.code,
                            description = message
                        )
                    )
                )
            }
        }

        return ResponseEntity.status(exception.codeError.httpStatus).body(view)
    }

    @ExceptionHandler(value = [Exception::class])
    private fun otherException(exception: Exception): ResponseEntity<View> {
        log.error(exception.message!!)

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                WebErrorView(
                    errors = listOf(
                        WebErrorView.Error(
                            code = CodesOfErrors.SERVER_ERROR.code,
                            description = "Unknown server error."
                        )
                    )
                )
            )
    }
}