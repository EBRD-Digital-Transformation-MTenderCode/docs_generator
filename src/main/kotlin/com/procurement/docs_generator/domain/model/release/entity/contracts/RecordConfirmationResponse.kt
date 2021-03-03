package com.procurement.docs_generator.domain.model.release.entity.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordConfirmationResponseValue

data class RecordConfirmationResponse(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: RecordConfirmationResponseValue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("request") @param:JsonProperty("request") val request: String?
)
