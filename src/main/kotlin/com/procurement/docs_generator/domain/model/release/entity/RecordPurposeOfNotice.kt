package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordPurposeOfNotice(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isACallForCompetition") @param:JsonProperty("isACallForCompetition") val isACallForCompetition: Boolean?

)

