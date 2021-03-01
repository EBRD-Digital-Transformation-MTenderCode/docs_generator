package com.procurement.docs_generator.domain.model.release.entity.auction

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordPeriod

data class RecordElectronicAuctionProgress(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("breakdown") @param:JsonProperty("breakdown") val breakdown: List<RecordElectronicAuctionProgressBreakdown> = emptyList()
)