package com.procurement.docs_generator.domain.model.release.entity.auction

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.Value

data class RecordElectronicAuctionResult(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedBid") @param:JsonProperty("relatedBid") val relatedBid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?
)