package com.procurement.docs_generator.domain.model.country

import com.procurement.docs_generator.domain.model.ValueObject

data class Country(val value: String) : ValueObject {
    override fun toString() = value
}