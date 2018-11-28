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

    private val RegistrationUri = endpointProperties.storage + "/storage/registration"
    private val UploadUri = endpointProperties.storage + "/storage/upload/"

    override fun upload(pdfDocument: PDFDocument): String {
        val fileName = pdfDocument.fileName
        val hash = pdfDocument.hash
        val weight = pdfDocument.size

        log.debug { "Registration document (file name: '$fileName', hash: '$hash', size: '$weight')." }
        val registrationResponse = webClient.post<RegistrationRequest, RegistrationResponse>(
            uri = URI(RegistrationUri),
            body = RegistrationRequest(fileName = fileName, hash = hash, weight = weight)
        )
        log.debug { "Document was registered (file name: '$fileName', hash: '$hash', size: '$weight')." }

        log.debug { "Upload document (file name: '$fileName', hash: '$hash', size: '$weight')." }
        val resp = webClient.sendFile<UploadResponse>(
            uri = URI(UploadUri + registrationResponse.data.id),
            pdfDocument = pdfDocument
        )
        log.debug { "Document was upload (file name: '$fileName', hash: '$hash', size: '$weight')." }

        return resp.data.id
    }
}