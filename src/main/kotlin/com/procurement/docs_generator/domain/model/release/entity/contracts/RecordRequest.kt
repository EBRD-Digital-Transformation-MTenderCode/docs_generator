package com.procurement.docs_generator.domain.model.release.entity.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordRelatedPerson

data class RecordRequest(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedPerson") @param:JsonProperty("relatedPerson") val relatedPerson: RecordRelatedPerson?
)