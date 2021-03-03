package com.procurement.docs_generator.domain.model.release.entity.tender

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.CoefficientRate
import com.procurement.docs_generator.domain.model.release.entity.CoefficientValue

data class RecordCoefficient(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("value") @param:JsonProperty("value") val value: CoefficientValue,
    @field:JsonProperty("coefficient") @param:JsonProperty("coefficient") val coefficient: CoefficientRate
)