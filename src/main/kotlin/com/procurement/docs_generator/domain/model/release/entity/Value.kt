package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.infrastructure.jackson.serializer.amount.AmountDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.amount.AmountSerializer
import java.math.BigDecimal

data class Value @JsonCreator constructor(

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var currency: String?,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amountNet: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val valueAddedTaxIncluded: Boolean?
)

