package com.procurement.docs_generator.domain.model.pmd

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.docs_generator.domain.model.EnumElementProvider

enum class RecordName(override val key: String) : EnumElementProvider.Key {
    AC("AC"),
    EV("EV"),
    FE("FE"),
    MS("MS");

    override fun toString(): String = key

    companion object : EnumElementProvider<RecordName>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = RecordName.orThrow(name)
    }
}
