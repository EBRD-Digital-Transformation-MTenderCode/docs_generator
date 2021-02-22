package com.procurement.docs_generator.domain.command

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.model.command.id.CommandId
import com.procurement.docs_generator.domain.model.command.id.CommandIdDeserializer
import com.procurement.docs_generator.domain.model.command.id.CommandIdSerializer
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.cpid.CPIDDeserializer
import com.procurement.docs_generator.domain.model.cpid.CPIDSerializer
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.ocid.OCIDDeserializer
import com.procurement.docs_generator.domain.model.ocid.OCIDSerializer
import com.procurement.docs_generator.domain.model.version.ApiVersion
import com.procurement.docs_generator.domain.model.version.ApiVersionDeserializer
import com.procurement.docs_generator.domain.model.version.ApiVersionSerializer

class GenerateDocumentResponse(
    @JsonDeserialize(using = ApiVersionDeserializer::class)
    @JsonSerialize(using = ApiVersionSerializer::class)
    @field:JsonProperty("version") @param:JsonProperty("version") val version: ApiVersion,

    @JsonDeserialize(using = CommandIdDeserializer::class)
    @JsonSerialize(using = CommandIdSerializer::class)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: CommandId,

    @field:JsonProperty("command") @param:JsonProperty("command") val name: String,

    @field:JsonProperty("data") @param:JsonProperty("data") val data: Data
) {
    @JsonPropertyOrder("cpid", "ocid", "documents", "documentInitiator")
    data class Data(
        @JsonDeserialize(using = CPIDDeserializer::class)
        @JsonSerialize(using = CPIDSerializer::class)
        @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: CPID,

        @JsonDeserialize(using = OCIDDeserializer::class)
        @JsonSerialize(using = OCIDSerializer::class)
        @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: OCID,

        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>,

        @field:JsonProperty("documentInitiator") @param:JsonProperty("documentInitiator") val documentInitiator: String
    ) {

        @JsonPropertyOrder("id")
        data class Document(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String
        )
    }
}