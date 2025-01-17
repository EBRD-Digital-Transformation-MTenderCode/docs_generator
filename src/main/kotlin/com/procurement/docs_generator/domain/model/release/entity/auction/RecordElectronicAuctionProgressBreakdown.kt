package com.procurement.docs_generator.domain.model.release.entity.auction

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.Value
import java.time.LocalDateTime

data class RecordElectronicAuctionProgressBreakdown(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedBid") @param:JsonProperty("relatedBid") val relatedBid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dateMet") @param:JsonProperty("dateMet") val dateMet: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?
)