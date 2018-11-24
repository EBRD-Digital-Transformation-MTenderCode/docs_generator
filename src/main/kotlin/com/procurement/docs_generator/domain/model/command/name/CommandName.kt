package com.procurement.docs_generator.domain.model.command.name

import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.ValueObject

enum class CommandName(val code: String) : ValueObject {
    GENERATE_AC_DOC("generateACDoc");

    companion object {
        private val map: Map<String, CommandName> = mutableMapOf<String, CommandName>().apply {
            enumValues<CommandName>().forEach { commandName ->
                this[commandName.code] = commandName
            }
        }

        fun valueOfCode(code: String): CommandName =
            map[code] ?: throw IllegalArgumentException("The command with code: '$code' not found.")
    }

    @JsonValue
    fun value(): String = code

    override fun toString(): String = code
}