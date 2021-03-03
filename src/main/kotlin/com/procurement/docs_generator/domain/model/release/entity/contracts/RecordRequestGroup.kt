package com.procurement.docs_generator.domain.model.release.entity.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordRequestGroup(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("requests") @param:JsonProperty("requests") val requests: List<RecordRequest> = emptyList()
)