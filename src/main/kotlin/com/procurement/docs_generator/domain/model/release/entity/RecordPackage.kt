package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonProperty

class RecordPackage(
    @field:JsonProperty("releases") @param:JsonProperty("releases") val releases: List<Record>
)