package com.procurement.docs_generator.domain.model.document

import com.procurement.docs_generator.domain.model.ValueObject
import com.procurement.docs_generator.domain.model.language.Language
import org.thymeleaf.context.IContext
import java.time.LocalDate

abstract class Document(
    val id: Id,
    val kind: Kind,
    val date: LocalDate,
    val lang: Language,
    val context: IContext
) {
    enum class Id(val code: String, val description: String) : ValueObject {
        AWARD_CONTRACT("AWARD-CONTRACT", "award contract");

        companion object {
            private val map: Map<String, Id> = mutableMapOf<String, Id>().apply {
                enumValues<Id>().forEach { this[it.code] = it }
            }

            fun valueOfCode(code: String): Id =
                map[code] ?: throw IllegalArgumentException("The document id with code: '$code' not found.")

            fun valueOfCodeOrNull(code: String): Id? = map[code]
        }
    }

    enum class Kind(val code: String, val description: String) : ValueObject {
        GOODS("GOODS", "kind is goods"),
        WORKS("WORKS", "kind is works"),
        SERVICES("SERVICES", "kind is service");

        companion object {
            private val map: Map<String, Kind> = mutableMapOf<String, Kind>().apply {
                enumValues<Kind>().forEach { this[it.code] = it }
            }

            fun valueOfCode(code: String): Kind =
                map[code] ?: throw IllegalArgumentException("The document kind with code: '$code' not found.")

            fun valueOfCodeOrNull(code: String): Kind? = map[code]
        }
    }
}