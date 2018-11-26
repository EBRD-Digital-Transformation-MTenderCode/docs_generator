package com.procurement.docs_generator.domain.model.document.id

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import com.procurement.docs_generator.domain.model.document.Document
import java.io.IOException

class DocumentIdDeserializer : ValueObjectDeserializer<Document.Id>() {
    companion object {
        fun deserialize(text: String) = Document.Id.valueOfCode(text.toUpperCase())
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Document.Id =
        DocumentIdDeserializer.deserialize(jsonParser.text)
}