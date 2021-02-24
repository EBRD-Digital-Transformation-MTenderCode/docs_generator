package com.procurement.docs_generator.domain.model.release.entity.tender

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordDimensions(
    @field:JsonProperty("requirementClassIdPR") @param:JsonProperty("requirementClassIdPR") val requirementClassIdPR: String
)