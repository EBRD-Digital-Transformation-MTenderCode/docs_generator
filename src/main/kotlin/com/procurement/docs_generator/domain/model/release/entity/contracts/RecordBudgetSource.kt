package com.procurement.docs_generator.domain.model.release.entity.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.infrastructure.jackson.serializer.amount.AmountDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.amount.AmountSerializer
import java.math.BigDecimal

data class RecordBudgetSource(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String?
)
