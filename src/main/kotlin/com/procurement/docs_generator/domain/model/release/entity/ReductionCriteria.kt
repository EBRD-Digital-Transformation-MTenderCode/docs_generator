package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class ReductionCriteria(@JsonValue override val key: String) : EnumElementProvider.Key {

    SCORING("scoring"),
    NONE("none");

    override fun toString(): String = key

    companion object : EnumElementProvider<ReductionCriteria>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = orThrow(name)
    }
}
