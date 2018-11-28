package com.procurement.docs_generator.domain.model.document

import org.springframework.core.io.FileSystemResource
import org.springframework.util.DigestUtils
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PDFDocument : Closeable {

    private val file: File = File(UUID.randomUUID().toString() + ".pdf").apply {
        createNewFile()
    }

    fun outputStream(): FileOutputStream = FileOutputStream(file)

    override fun close() {
        file.delete()
    }

    val resource: FileSystemResource
        get() = FileSystemResource(file)

    val fileName: String
        get() = file.name

    val hash: String
        get() = DigestUtils.md5DigestAsHex(file.inputStream())

    val size: Long
        get() = file.length()
}
