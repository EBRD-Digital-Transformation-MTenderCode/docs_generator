package com.procurement.docs_generator.domain.model.release.entity.tender.criteria

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.CriteriaSource
import com.procurement.docs_generator.domain.model.release.entity.RecordClassification
import com.procurement.docs_generator.domain.model.release.entity.RecordRequirementGroup

data class RecordCriteria(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("source") @param:JsonProperty("source") val source: CriteriaSource?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("requirementGroups") @param:JsonProperty("requirementGroups") val requirementGroups: List<RecordRequirementGroup> = emptyList(),

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: RecordClassification?
)
