package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.RequirementDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.RequirementSerializer

data class RecordRequirementGroup(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonDeserialize(using = RequirementDeserializer::class)
    @JsonSerialize(using = RequirementSerializer::class)
    @field:JsonProperty("requirements") @param:JsonProperty("requirements") val requirements: List<Requirement> = emptyList()
)

