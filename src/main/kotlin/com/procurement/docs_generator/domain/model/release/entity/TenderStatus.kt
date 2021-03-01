package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class TenderStatus(@JsonValue override val key: String) : EnumElementProvider.Key {
    ACTIVE("active"),
    CANCELLED("cancelled"),
    COMPLETE("complete"),
    PLANNED("planned"),
    PLANNING("planning"),
    UNSUCCESSFUL("unsuccessful"),
    WITHDRAWN("withdrawn");

    override fun toString(): String = this.key

    companion object : EnumElementProvider<TenderStatus>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = TenderStatus.orThrow(name)
    }
}
