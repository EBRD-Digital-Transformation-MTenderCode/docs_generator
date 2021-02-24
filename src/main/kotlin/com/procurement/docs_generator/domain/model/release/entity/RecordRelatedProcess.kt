package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType

data class RecordRelatedProcess(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relationship") @param:JsonProperty("relationship") val relationship: List<RelatedProcessType> = emptyList(),

    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: RelatedProcessScheme,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
)
