package com.procurement.docs_generator.domain.view.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.docs_generator.domain.view.View

@JsonPropertyOrder("errors")
class WebErrorView(
    @field:JsonProperty("errors") @param:JsonProperty("errors") val errors: List<Error>
) : View {
    @JsonPropertyOrder("code", "description")
    data class Error(
        @field:JsonProperty("code") @param:JsonProperty("code") private val code: String,
        @field:JsonProperty("description") @param:JsonProperty("description") private val description: String
    )
}