package com.procurement.docs_generator.domain.model.release.entity.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.model.release.entity.RecordOrganizationReference
import com.procurement.docs_generator.domain.model.release.entity.RecordPeriod
import com.procurement.docs_generator.domain.model.release.entity.RequirementRsValue
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.value.RequirementValueDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.value.RequirementValueSerializer

data class RecordRequirementResponse(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonDeserialize(using = RequirementValueDeserializer::class)
    @JsonSerialize(using = RequirementValueSerializer::class)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: RequirementRsValue,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("requirement") @param:JsonProperty("requirement") val requirement: RecordRequirementReference?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @param:JsonProperty("evidences") @field:JsonProperty("evidences") val evidences: List<RecordEvidence> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("responder") @field:JsonProperty("responder") val responder: RecordResponder?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedTenderer") @param:JsonProperty("relatedTenderer") val relatedTenderer: RecordOrganizationReference?
)
