package com.procurement.docs_generator.domain.command

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
import com.procurement.docs_generator.domain.model.version.ApiVersion
import com.procurement.docs_generator.domain.model.version.ApiVersionDeserializer
import com.procurement.docs_generator.domain.model.version.ApiVersionSerializer

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder("version", "id", "command")
class Command(
    @JsonDeserialize(using = ApiVersionDeserializer::class)
    @JsonSerialize(using = ApiVersionSerializer::class)
    @field:JsonProperty("version") @param:JsonProperty("version") val version: ApiVersion,

    @JsonDeserialize(using = CommandIdDeserializer::class)
    @JsonSerialize(using = CommandIdSerializer::class)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: CommandId,

    @JsonDeserialize(using = CommandNameDeserializer::class)
    @JsonSerialize(using = CommandNameSerializer::class)
    @field:JsonProperty("command") @param:JsonProperty("command") val name: CommandName
)
