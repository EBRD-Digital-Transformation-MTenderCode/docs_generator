package com.procurement.docs_generator.domain.model.release.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordMainEconomicActivity (
    @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
    @param:JsonProperty("description") @field:JsonProperty("description") val description: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
)