package com.procurement.docs_generator.infrastructure.adapter

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.procurement.docs_generator.adapter.RemoteClient
import com.procurement.docs_generator.adapter.UploadDocumentAdapter
import com.procurement.docs_generator.adapter.post
import com.procurement.docs_generator.adapter.sendFile
import com.procurement.docs_generator.configuration.properties.EndpointProperties
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.document.PDFDocument
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

data class RegistrationRequest(
    val fileName: String,
    val hash: String,
    val weight: Long
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RegistrationResponse(
    val data: Data
) {
    data class Data(
        val id: String,
        val url: String,
        val dateModified: String
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class UploadResponse(
    val data: Data
) {
    data class Data(
        val id: String,
        val url: String,
        val dateModified: String
    )
}

@Service
class SimpleUploadDocumentAdapter(
    endpointProperties: EndpointProperties,
    private val webClient: RemoteClient
) : UploadDocumentAdapter {

    companion object {
        private val log: Logger = Slf4jLogger()
    }

    private val registrationUri: URI = endpointProperties.storage?.registration?.let {
        UriComponentsBuilder.fromHttpUrl(it)
            .pathSegment("storage")
            .pathSegment("registration")
            .build(emptyMap<String, Any>())
    } ?: throw IllegalStateException("URI to storage-registration not set.")

    private val uploadUri: URI = endpointProperties.storage?.upload?.let {
        UriComponentsBuilder.fromHttpUrl(it)
            .pathSegment("storage")
            .pathSegment("upload")
            .build(emptyMap<String, Any>())
    } ?: throw IllegalStateException("URI to storage-upload not set.")

    override fun upload(pdfDocument: PDFDocument): String {
        val fileName = pdfDocument.fileName
        val hash = pdfDocument.hash
        val weight = pdfDocument.size

        log.debug { "Registration document (file name: '$fileName', hash: '$hash', size: '$weight')." }
        val registrationResponse = webClient.post<RegistrationRequest, RegistrationResponse>(
            uri = registrationUri,
            body = RegistrationRequest(fileName = fileName, hash = hash, weight = weight)
        )
        log.debug { "Document was registered (file name: '$fileName', hash: '$hash', size: '$weight')." }

        log.debug { "Upload document (file name: '$fileName', hash: '$hash', size: '$weight')." }

        val resp = webClient.sendFile<UploadResponse>(
            uri = UriComponentsBuilder.fromUri(uploadUri)
                .pathSegment(registrationResponse.data.id)
                .build(emptyMap<String, Any>()),
            pdfDocument = pdfDocument
        )
        log.debug { "Document was upload (file name: '$fileName', hash: '$hash', size: '$weight')." }

        return resp.data.id
    }
}