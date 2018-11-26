package com.procurement.docs_generator.domain.model.document.kind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import com.procurement.docs_generator.domain.model.document.Document
import java.io.IOException

class DocumentKindDeserializer : ValueObjectDeserializer<Document.Kind>() {
    companion object {
        fun deserialize(text: String) = Document.Kind.valueOfCode(text.toUpperCase())
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Document.Kind =
        DocumentKindDeserializer.deserialize(jsonParser.text)
}