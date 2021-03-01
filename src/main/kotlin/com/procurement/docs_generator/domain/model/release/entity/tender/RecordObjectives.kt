package com.procurement.docs_generator.domain.model.release.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordObjectives(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("types") @param:JsonProperty("types") val types: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("additionalInformation") @param:JsonProperty("additionalInformation") val additionalInformation: String?
)
