package com.procurement.docs_generator.domain.model.template

import com.procurement.docs_generator.domain.model.ValueObject
import java.nio.ByteBuffer
import java.time.LocalDate

data class Template(
    val startDate: LocalDate,
    val format: Format,
    val engine: Engine,
    val body: ByteBuffer
) {
    enum class Format(val code: String, val description: String) : ValueObject {
        HTML(code = "HTML", description = "HTML format"),
        DOCX(code = "DOCX", description = "DOCX format"),
        ODT(code = "ODT", description = "ODT format");

        companion object {
            private val map: Map<String, Format> = mutableMapOf<String, Format>().apply {
                enumValues<Format>().forEach { this[it.code] = it }
            }

            fun valueOfCode(code: String): Format =
                map[code] ?: throw IllegalArgumentException("The template format with code: '$code' not found.")
        }
    }

    enum class Engine(val code: String, val description: String) : ValueObject {
        Thymeleaf(code = "THYMELEAF", description = "Thymeleaf template engine"),
        Velocity(code = "VELOCITY", description = "Velocity template engine"),
        Freemarker(code = "FREEMARKER", description = "Freemarker template engine");

        companion object {
            private val map: Map<String, Engine> = mutableMapOf<String, Engine>().apply {
                enumValues<Engine>().forEach { this[it.code] = it }
            }

            fun valueOfCode(code: String): Engine =
                map[code] ?: throw IllegalArgumentException("The template engine with code: '$code' not found.")
        }
    }
}