package com.procurement.docs_generator.infrastructure.common

import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

fun InputStream.toByteBuffer(): ByteBuffer {
    val byteArray = this.readBytes()
    return ByteBuffer.wrap(byteArray)
}

fun String.toByteBuffer(text: String, charset: Charset): ByteBuffer {
    return ByteBuffer.wrap(text.toByteArray(charset))
}

fun ByteBuffer.toString(charset: Charset): String {
    val bytes: ByteArray
    if (this.hasArray()) {
        bytes = this.array()
    } else {
        bytes = ByteArray(this.remaining())
        this.get(bytes)
    }
    return String(bytes, charset)
}



