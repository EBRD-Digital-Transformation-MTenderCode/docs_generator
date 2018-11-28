package com.procurement.docs_generator.infrastructure.adapter

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.procurement.docs_generator.application.service.template.TemplateService
import com.procurement.docs_generator.configuration.properties.GlobalProperties
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.exception.template.TemplateAlreadyException
import com.procurement.docs_generator.exception.template.TemplateUpdateException
import org.hamcrest.core.IsEqual
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder
import java.time.LocalDate

@ExtendWith(RestDocumentationExtension::class)
class TemplateControllerTest {
    companion object {
        var file = MockMultipartFile("file", "template.html", "text/html", "FILE_WITH_TEMPLATE".toByteArray())
    }

    private lateinit var mockMvc: MockMvc
    private lateinit var templateService: TemplateService

    @BeforeEach
    fun init(restDocumentation: RestDocumentationContextProvider) {
        templateService = mock()

        val controller = TemplateController(templateService = templateService)
        val exceptionHandler = WebExceptionHandler()

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(exceptionHandler)
            .apply<StandaloneMockMvcBuilder>(
                MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                    .uris()
                    .and()
                    .snippets()
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(Preprocessors.prettyPrint())
                    .withResponseDefaults(Preprocessors.prettyPrint())
            ).build()
    }

    @Nested
    inner class AddTemplate {
        @Test
        @DisplayName("Add template was successful")
        fun addTemplateSuccessful() {
            val uri = getURI()
            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data").isEmpty)
                .andDo(
                    document(
                        "template/add/success",
                        responseFields(ModelDescription.AddTemplate.responseSuccessful())
                    )
                )
        }

        @Test
        @DisplayName("Unknown id")
        fun unknownId() {
            val uri = getURI(id = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'id'.")
                    )
                )
                .andDo(
                    document(
                        "template/add/errors/invalid-id",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Unknown kind")
        fun unknownKind() {
            val uri = getURI(kind = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'kind'.")
                    )
                )
                .andDo(
                    document(
                        "template/add/errors/invalid-kind",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Invalid date")
        fun invalidDate() {
            val uri = getURI(date = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'date'.")
                    )
                )
                .andDo(
                    document(
                        "template/add/errors/invalid-date",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Template is already")
        fun templateIsAlready() {
            val id = Document.Id.AWARD_CONTRACT
            val kind = Document.Kind.SERVICES
            val lang = Language("RO")
            val date = LocalDate.now()

            val uri = getURI(id = id.code, kind = kind.code, lang = lang.value, date = date.toString())

            whenever(templateService.add(id = any(),
                                         kind = any(),
                                         lang = any(),
                                         date = any(),
                                         file = any(),
                                         format = any(),
                                         engine = any()))
                .thenThrow(TemplateAlreadyException(id = id, kind = kind, lang = lang, date = date))

            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.03.02")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Template is already (id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date').")
                    )
                )
                .andDo(
                    document(
                        "template/add/errors/template-already",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }
    }

    @Nested
    inner class UpdateTemplate {

        @Test
        @DisplayName("Update template was successful")
        fun updateTemplateSuccessful() {
            val uri = getURI()

            mockMvc.perform(
                mockMultipartBuilder(uri = uri, method = "PUT")
                    .file(file))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.data").isEmpty)
                .andDo(
                    document(
                        "template/update/success",
                        responseFields(ModelDescription.UpdateTemplate.responseSuccessful())
                    )
                )
        }

        @Test
        @DisplayName("Unknown id")
        fun unknownId() {
            val uri = getURI(id = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri, method = "PUT")
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'id'.")
                    )
                )
                .andDo(
                    document(
                        "template/update/errors/invalid-id",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Unknown kind")
        fun unknownKind() {
            val uri = getURI(kind = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri, method = "PUT")
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'kind'.")
                    )
                )
                .andDo(
                    document(
                        "template/update/errors/invalid-kind",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Invalid date")
        fun invalidDate() {
            val uri = getURI(date = "UNKNOWN")
            mockMvc.perform(
                mockMultipartBuilder(uri = uri, method = "PUT")
                    .file(file))
                .andExpect(status().isBadRequest)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("400.${GlobalProperties.serviceId}.01.01")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Invalid value: 'UNKNOWN' of path variable: 'date'.")
                    )
                )
                .andDo(
                    document(
                        "template/update/errors/invalid-date",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }

        @Test
        @DisplayName("Error of store a template")
        fun errorStoreTemplate() {
            val id = Document.Id.AWARD_CONTRACT
            val kind = Document.Kind.SERVICES
            val lang = Language("RO")
            val date = LocalDate.now()

            val uri = getURI(id = id.code, kind = kind.code, lang = lang.value, date = date.toString())

            whenever(templateService.add(id = any(),
                                         kind = any(),
                                         lang = any(),
                                         date = any(),
                                         file = any(),
                                         format = any(),
                                         engine = any()))
                .thenThrow(TemplateUpdateException(id = id, kind = kind, lang = lang, date = date))

            mockMvc.perform(
                mockMultipartBuilder(uri = uri)
                    .file(file))
                .andExpect(status().isInternalServerError)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errors").isNotEmpty)
                .andExpect(jsonPath("$.errors.length()", IsEqual.equalTo(1)))
                .andExpect(jsonPath("$.errors[0].code", IsEqual.equalTo("500.${GlobalProperties.serviceId}.03.03")))
                .andExpect(
                    jsonPath(
                        "$.errors[0].description",
                        IsEqual.equalTo("Template update error (id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date').")
                    )
                )
                .andDo(
                    document(
                        "template/update/errors/error-store",
                        responseFields(ModelDescription.responseError())
                    )
                )
        }
    }

    private fun mockMultipartBuilder(uri: String, method: String = "POST"): MockMultipartHttpServletRequestBuilder {
        return MockMvcRequestBuilders.multipart(uri).apply {
            this.with { request ->
                request.method = method
                request
            }
        }
    }

    private fun getURI(endPoint: String = "templates",
                       id: String = "AWARD-CONTRACT",
                       kind: String = "GOODS",
                       lang: String = "RO",
                       date: String = "2018-11-27"): String = buildString {
        this.append("/")
        this.append(endPoint)

        if (id.isNotBlank()) {
            this.append("/")
            this.append(id)
        }

        if (id.isNotBlank()) {
            this.append("/")
            this.append(kind)
        }

        if (id.isNotBlank()) {
            this.append("/")
            this.append(lang)
        }

        if (id.isNotBlank()) {
            this.append("/")
            this.append(date)
        }
    }
}