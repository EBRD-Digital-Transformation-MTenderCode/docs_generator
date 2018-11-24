package com.procurement.docs_generator.domain.model.document.mode

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import java.io.IOException

class TemplateModeSerializer : ValueObjectSerializer<Mode>() {
    companion object {
        fun serialize(mode: Mode) = mode.code
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(mode: Mode, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(TemplateModeSerializer.serialize(mode))
}