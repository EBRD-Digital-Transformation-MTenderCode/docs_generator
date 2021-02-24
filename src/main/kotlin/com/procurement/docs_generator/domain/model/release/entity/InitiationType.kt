package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

enum class InitiationType constructor(private val value: String) {

    TENDER("tender");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, InitiationType>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): InitiationType {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}