package com.procurement.docs_generator.domain.view.web

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.docs_generator.domain.view.View

@JsonPropertyOrder("data")
class AddedTemplateWebView(
    @field:JsonProperty("data") @param:JsonProperty("data") val data: Data = Data()
) : View {
    class Data : View
}