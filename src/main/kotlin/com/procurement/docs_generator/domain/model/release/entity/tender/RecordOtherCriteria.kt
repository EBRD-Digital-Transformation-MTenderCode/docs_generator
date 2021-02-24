package com.procurement.docs_generator.domain.model.release.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.QualificationSystemMethod
import com.procurement.docs_generator.domain.model.release.entity.ReductionCriteria

data class RecordOtherCriteria(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("reductionCriteria") @param:JsonProperty("reductionCriteria") val reductionCriteria: ReductionCriteria? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("qualificationSystemMethods") @param:JsonProperty("qualificationSystemMethods") val qualificationSystemMethods: List<QualificationSystemMethod> = emptyList()
)
