package com.procurement.docs_generator.domain.model.document.mode

import com.procurement.docs_generator.domain.model.ValueObject

enum class Mode(val code: String, val description: String) : ValueObject {
    HTML(code = "HTML", description = "HTML mode");

    companion object {
        private val map: Map<String, Mode> = mutableMapOf<String, Mode>().apply {
            enumValues<Mode>().forEach { this[it.code] = it }
        }

        fun valueOfCode(code: String): Mode =
            map[code] ?: throw IllegalArgumentException("The template mode with code: '$code' not found.")
    }
}