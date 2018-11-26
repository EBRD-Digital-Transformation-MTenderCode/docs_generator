package com.procurement.docs_generator.domain.model.command.id

import com.procurement.docs_generator.domain.model.ValueObject
import java.util.*

data class CommandId(val value: UUID) : ValueObject {
    override fun toString(): String = value.toString()
}