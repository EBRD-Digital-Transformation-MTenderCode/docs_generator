package com.procurement.docs_generator.adapter

import com.procurement.docs_generator.domain.model.document.PDFDocument
import org.springframework.http.HttpHeaders
import java.net.URI

inline fun <reified T> RemoteClient.get(uri: URI, headers: HttpHeaders = HttpHeaders.EMPTY): T =
    this.get(uri, headers, T::class.java)

inline fun <V, reified T> RemoteClient.post(uri: URI, headers: HttpHeaders = HttpHeaders.EMPTY, body: V): T =
    this.post(uri, headers, body, T::class.java)

inline fun <reified T> RemoteClient.sendFile(uri: URI, pdfDocument: PDFDocument): T =
    this.sendFile(uri, pdfDocument, T::class.java)

interface RemoteClient {
    fun <T> get(uri: URI, headers: HttpHeaders = HttpHeaders.EMPTY, targetType: Class<T>): T

    fun <V, T> post(uri: URI, headers: HttpHeaders, body: V, targetType: Class<T>): T

    fun <T> sendFile(uri: URI, pdfDocument: PDFDocument, targetType: Class<T>): T
}