package com.procurement.docs_generator.domain.model.release.entity.address

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordLocalityDetails(

    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
)