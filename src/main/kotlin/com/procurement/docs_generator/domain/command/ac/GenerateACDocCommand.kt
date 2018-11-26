package com.procurement.docs_generator.domain.command.ac

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.model.command.id.CommandId
import com.procurement.docs_generator.domain.model.command.id.CommandIdDeserializer
import com.procurement.docs_generator.domain.model.command.id.CommandIdSerializer
import com.procurement.docs_generator.domain.model.command.name.CommandName
import com.procurement.docs_generator.domain.model.command.name.CommandNameDeserializer
import com.procurement.docs_generator.domain.model.command.name.CommandNameSerializer
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.country.CountryDeserializer
import com.procurement.docs_generator.domain.model.country.CountrySerializer
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.cpid.CPIDDeserializer
import com.procurement.docs_generator.domain.model.cpid.CPIDSerializer
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.language.LanguageDeserializer
import com.procurement.docs_generator.domain.model.language.LanguageSerializer
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.ocid.OCIDDeserializer
import com.procurement.docs_generator.domain.model.ocid.OCIDSerializer
import com.procurement.docs_generator.domain.model.version.ApiVersion
import com.procurement.docs_generator.domain.model.version.ApiVersionDeserializer
import com.procurement.docs_generator.domain.model.version.ApiVersionSerializer

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder("version", "id", "command", "data")
data class GenerateACDocCommand(
    @JsonDeserialize(using = ApiVersionDeserializer::class)
    @JsonSerialize(using = ApiVersionSerializer::class)
    @field:JsonProperty("version") @param:JsonProperty("version") val version: ApiVersion,

    @JsonDeserialize(using = CommandIdDeserializer::class)
    @JsonSerialize(using = CommandIdSerializer::class)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: CommandId,

    @JsonDeserialize(using = CommandNameDeserializer::class)
    @JsonSerialize(using = CommandNameSerializer::class)
    @field:JsonProperty("command") @param:JsonProperty("command") val name: CommandName,

    @field:JsonProperty("data") @param:JsonProperty("data") val data: GenerateACDocCommand.Data
) {
    @JsonPropertyOrder("country", "language", "cpid", "ocid")
    class Data(
        @JsonDeserialize(using = CountryDeserializer::class)
        @JsonSerialize(using = CountrySerializer::class)
        @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,

        @JsonDeserialize(using = LanguageDeserializer::class)
        @JsonSerialize(using = LanguageSerializer::class)
        @field:JsonProperty("language") @param:JsonProperty("language") val language: Language,

        @JsonDeserialize(using = CPIDDeserializer::class)
        @JsonSerialize(using = CPIDSerializer::class)
        @field:JsonProperty("cpid") @param:JsonProperty("cpid") val cpid: CPID,

        @JsonDeserialize(using = OCIDDeserializer::class)
        @JsonSerialize(using = OCIDSerializer::class)
        @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: OCID
    )
}