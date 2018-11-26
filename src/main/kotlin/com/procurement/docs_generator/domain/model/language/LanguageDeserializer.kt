package com.procurement.docs_generator.domain.model.language

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import java.io.IOException

class LanguageDeserializer : ValueObjectDeserializer<Language>() {
    companion object {

        fun deserialize(text: String): Language {
            return Language(text.toUpperCase())
        }
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Language =
        LanguageDeserializer.deserialize(jsonParser.text)
}