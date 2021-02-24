package com.procurement.docs_generator.domain.model.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.docs_generator.domain.model.EnumElementProvider
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType

class ValueEntity(
    val pmd: ProcurementMethod,
    val documentInitiator: String,
    val parameter: Parameter,
    val record: RecordName,
    val path: String
) {
    class Relationships(values: List<RelatedProcessType>) : List<RelatedProcessType> by values

    enum class Parameter(@JsonValue override val key: String) : EnumElementProvider.Key {

        DATE("date"),
        SUBGROUP("subGroup");

        override fun toString(): String = this.key

        companion object : EnumElementProvider<Parameter>(info = info()) {

            @JvmStatic
            @JsonCreator
            fun creator(name: String) = Parameter.orThrow(name)
        }
    }
}
