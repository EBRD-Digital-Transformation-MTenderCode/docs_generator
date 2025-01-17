package com.procurement.docs_generator.domain.model.release.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.auction.RecordElectronicAuctionsDetails

data class RecordElectronicAuctions(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<RecordElectronicAuctionsDetails> = emptyList()
)
