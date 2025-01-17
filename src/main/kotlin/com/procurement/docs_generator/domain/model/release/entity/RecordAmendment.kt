package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.documents.RecordDocument
import java.time.LocalDateTime

data class RecordAmendment(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("type") @param:JsonProperty("type") val type: AmendmentType?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: AmendmentStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: AmendmentRelatesTo?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("releaseID") @param:JsonProperty("releaseID") val releaseID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amendsReleaseID") @param:JsonProperty("amendsReleaseID") val amendsReleaseID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String?,

    @Deprecated("Use 'relatesTo' & 'relatedItem' instead of this. ")
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("changes") @param:JsonProperty("changes") val changes: List<RecordChange> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RecordDocument> = emptyList()

)
