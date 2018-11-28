package com.procurement.docs_generator.domain.model.version

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException

class ApiVersionDeserializer : JsonDeserializer<ApiVersion>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ApiVersion =
        ApiVersion.valueOf(jsonParser.text)
}