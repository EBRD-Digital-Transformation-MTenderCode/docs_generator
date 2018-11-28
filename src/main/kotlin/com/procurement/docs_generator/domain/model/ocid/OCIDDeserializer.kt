package com.procurement.docs_generator.domain.model.ocid

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import java.io.IOException

class OCIDDeserializer : ValueObjectDeserializer<OCID>() {
    companion object {
        fun deserialize(text: String) = OCID(text)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): OCID =
        OCIDDeserializer.deserialize(jsonParser.text)
}