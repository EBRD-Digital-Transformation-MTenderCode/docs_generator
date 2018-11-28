package com.procurement.docs_generator.domain.model.language

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import java.io.IOException

class LanguageSerializer : ValueObjectSerializer<Language>() {
    companion object {
        fun serialize(language: Language): String = language.value
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(language: Language, jsonGenerator: JsonGenerator, provider: SerializerProvider) {
        jsonGenerator.writeString(LanguageSerializer.serialize(language))
    }
}