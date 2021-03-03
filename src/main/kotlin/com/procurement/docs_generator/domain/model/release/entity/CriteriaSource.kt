package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class CriteriaSource(@JsonValue override val key: String) : EnumElementProvider.Key {
    TENDERER("tenderer"),
    BUYER("buyer"),
    PROCURING_ENTITY("procuringEntity");

    override fun toString(): String = key

    companion object : EnumElementProvider<CriteriaSource>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = CriteriaSource.orThrow(name)
    }
}

