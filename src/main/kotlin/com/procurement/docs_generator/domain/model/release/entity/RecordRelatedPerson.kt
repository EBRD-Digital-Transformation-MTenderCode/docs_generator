package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordRelatedPerson(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
)