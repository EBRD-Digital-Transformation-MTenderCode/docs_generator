package com.procurement.docs_generator.domain.model.release.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordIssue
import com.procurement.docs_generator.domain.model.release.entity.RecordPeriod

data class RecordPermitDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedBy") @param:JsonProperty("issuedBy") val issuedBy: RecordIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedThought") @param:JsonProperty("issuedThought") val issuedThought: RecordIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("validityPeriod") @param:JsonProperty("validityPeriod") val validityPeriod: RecordPeriod?
)