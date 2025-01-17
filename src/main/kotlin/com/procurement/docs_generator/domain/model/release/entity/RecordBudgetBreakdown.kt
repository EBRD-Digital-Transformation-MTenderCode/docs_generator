package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordBudgetBreakdown(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("sourceParty") @param:JsonProperty("sourceParty") val sourceParty: RecordOrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("europeanUnionFunding") @param:JsonProperty("europeanUnionFunding") val europeanUnionFunding: RecordEuropeanUnionFunding?
)
