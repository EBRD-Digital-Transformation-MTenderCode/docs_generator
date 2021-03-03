package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class RequirementDataType(@JsonValue override val key: String) : EnumElementProvider.Key {

    BOOLEAN("boolean"),
    STRING("string"),
    NUMBER("number"),
    INTEGER("integer");

    override fun toString(): String = this.key

    companion object : EnumElementProvider<RequirementDataType>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = RequirementDataType.orThrow(name)
    }
}
