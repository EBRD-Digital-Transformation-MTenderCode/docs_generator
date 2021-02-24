package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class AmendmentRelatesTo(@JsonValue override val key: String) : EnumElementProvider.Key {
    LOT("lot"),
    TENDER("tender"),
    CAN("can");

    override fun toString(): String = key

    companion object : EnumElementProvider<AmendmentRelatesTo>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = AmendmentRelatesTo.orThrow(name)
    }
}
