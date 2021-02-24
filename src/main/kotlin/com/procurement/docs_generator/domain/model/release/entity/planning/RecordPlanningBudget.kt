package com.procurement.docs_generator.domain.model.release.entity.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordBudgetAllocation
import com.procurement.docs_generator.domain.model.release.entity.RecordBudgetBreakdown
import com.procurement.docs_generator.domain.model.release.entity.Value

data class RecordPlanningBudget(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("budgetAllocation") @param:JsonProperty("budgetAllocation") val budgetAllocation: List<RecordBudgetAllocation> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("budgetSource") @param:JsonProperty("budgetSource") val budgetSource: List<RecordPlanningBudgetSource> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Value?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isEuropeanUnionFunded") @param:JsonProperty("isEuropeanUnionFunded") val isEuropeanUnionFunded: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("budgetBreakdown") @param:JsonProperty("budgetBreakdown") val budgetBreakdown: List<RecordBudgetBreakdown> = emptyList()
)