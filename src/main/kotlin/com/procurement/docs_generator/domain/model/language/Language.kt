package com.procurement.docs_generator.domain.model.language

import com.procurement.docs_generator.domain.model.ValueObject

data class Language(val value: String) : ValueObject {
    override fun toString() = value
}