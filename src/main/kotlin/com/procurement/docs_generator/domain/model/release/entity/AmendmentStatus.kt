package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class AmendmentStatus(@JsonValue override val key: String) : EnumElementProvider.Key {

    PENDING("pending"),
    ACTIVE("active"),
    WITHDRAWN("withdrawn"),
    CANCELLED("cancelled");

    override fun toString(): String = key

    companion object : EnumElementProvider<AmendmentStatus>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = AmendmentStatus.orThrow(name)
    }
}

