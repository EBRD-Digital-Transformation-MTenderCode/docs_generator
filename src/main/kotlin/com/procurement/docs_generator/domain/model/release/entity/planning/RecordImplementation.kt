package com.procurement.docs_generator.domain.model.release.entity.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordImplementation(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("transactions") @param:JsonProperty("transactions") val transactions: List<RecordTransaction> = emptyList()
)