package com.procurement.docs_generator.domain.model.release

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder("releases")
class MSReleasesPackage(
    @field:JsonProperty("releases") @param:JsonProperty("releases") val releases: List<Release>
) {
    @JsonPropertyOrder("tender")
    data class Release(
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender
    ) {
        @JsonPropertyOrder("id", "title", "mainProcurementCategory", "value")
        data class Tender(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
            @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
        ) {
            @JsonPropertyOrder("amount")
            data class Value(
                @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal
            )
        }
    }
}