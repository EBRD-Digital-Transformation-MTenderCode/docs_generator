package com.procurement.docs_generator.domain.model.release

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder("publishedDate", "releases")
class EVReleasesPackage(
    @JsonProperty("publishedDate") val publishedDate: String,
    @field:JsonProperty("releases") @param:JsonProperty("releases") val releases: List<Release>
) {
    @JsonPropertyOrder("tender")
    data class Release(
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender
    ) {
        @JsonPropertyOrder("id")
        data class Tender(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String
        )
    }
}