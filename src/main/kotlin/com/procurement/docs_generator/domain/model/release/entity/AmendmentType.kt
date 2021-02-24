package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class AmendmentType(@JsonValue override val key: String) : EnumElementProvider.Key {

    CANCELLATION("cancellation"),
    TENDER_CHANGE("tenderChange");

    override fun toString(): String = key

    companion object : EnumElementProvider<AmendmentType>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = AmendmentType.orThrow(name)
    }
}
