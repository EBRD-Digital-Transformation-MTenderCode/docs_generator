package com.procurement.docs_generator.infrastructure.adapter

import com.fasterxml.jackson.databind.JsonMappingException
import com.procurement.docs_generator.adapter.RemoteClient
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.exception.remote.RemoteServiceException
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

@Service
class RestRemoteClient(
    private val webClient: RestTemplate,
    private val transformService: TransformService
) : RemoteClient {

    override fun <T> get(uri: URI, headers: HttpHeaders, targetType: Class<T>): T {
        val entity = HttpEntity<Unit>(headers)
        return exchangeAndTransform(uri = uri, method = HttpMethod.GET, entity = entity, targetType = targetType)
    }

    override fun <V, T> post(uri: URI, headers: HttpHeaders, body: V, targetType: Class<T>): T {
        val entity = HttpEntity(body, headers)
        return exchangeAndTransform(uri = uri, method = HttpMethod.POST, entity = entity, targetType = targetType)
    }

    override fun <T> sendFile(uri: URI,
                              pdfDocument: PDFDocument,
                              targetType: Class<T>): T {
        try {
            val headers = HttpHeaders()
            headers.contentType = MediaType.MULTIPART_FORM_DATA
            val body = LinkedMultiValueMap<String, Any>()
            body.add("file", pdfDocument.resource)
            val requestEntity = HttpEntity(body, headers)
            val response = webClient.exchange(uri, HttpMethod.POST, requestEntity, targetType)

            return response.body!!
        } catch (exception: HttpClientErrorException) {
            val code = exception.statusCode
            val payload = exception.responseBodyAsString
            val message = "Error [$code:$payload] of remote service by uri: '$uri'."

            throw RemoteServiceException(
                code = code,
                payload = payload,
                message = message,
                exception = exception
            )
        } catch (exception: Exception) {
            throw RemoteServiceException(
                message = "Web client error when accessing remote service by uri: '$uri'.",
                exception = exception
            )
        }
    }
    private fun <T, V> exchangeAndTransform(uri: URI,
                                            method: HttpMethod,
                                            entity: HttpEntity<V>,
                                            targetType: Class<T>): T {
        val response: ResponseEntity<String> = exchange(uri, method, entity)
        val body = response.body
        if (body == null || body.isEmpty())
            throw RemoteServiceException(
                message = "Error of remote service by uri: '$uri'. Received empty body.",
                payload = response.body
            )
        return transform(body, targetType, uri)
    }

    fun <V> exchange(uri: URI, method: HttpMethod, entity: HttpEntity<V>): ResponseEntity<String> =
        try {
            webClient.exchange(uri, method, entity, String::class.java)
        } catch (exception: HttpClientErrorException) {
            val code = exception.statusCode
            val payload = exception.responseBodyAsString
            val message = "Error [$code:$payload] of remote service by uri: '$uri'."
            throw RemoteServiceException(
                code = code,
                payload = payload,
                message = message,
                exception = exception
            )
        } catch (exception: Exception) {
            throw RemoteServiceException(
                message = "Error of remote service by uri: '$uri'.",
                exception = exception
            )
        }

    fun<T> transform(payload: String, targetType: Class<T>, uri: URI): T {
        return try {
            transformService.deserialize(payload, targetType)
        } catch (exception : JsonMappingException) {
            throw RemoteServiceException(
                message = "Error parsing response from remote service by uri: '$uri'.",
                exception = exception,
                payload = payload
            )
        }
    }
}