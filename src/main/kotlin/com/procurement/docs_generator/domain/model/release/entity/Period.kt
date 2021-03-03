package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Period @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val startDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val endDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maxExtentDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val durationInDays: Int?
)
