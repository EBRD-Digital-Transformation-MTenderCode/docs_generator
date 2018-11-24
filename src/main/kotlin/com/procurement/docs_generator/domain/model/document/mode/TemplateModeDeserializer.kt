package com.procurement.docs_generator.domain.model.document.mode

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import java.io.IOException

class TemplateModeDeserializer : ValueObjectDeserializer<Mode>() {
    companion object {
        fun deserialize(text: String) = Mode.valueOfCode(text)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Mode =
        TemplateModeDeserializer.deserialize(jsonParser.text)
}