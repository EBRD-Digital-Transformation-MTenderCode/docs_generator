package com.procurement.docs_generator.domain.model.document.id

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import com.procurement.docs_generator.domain.model.document.Document
import java.io.IOException

class DocumentIdSerializer : ValueObjectSerializer<Document.Id>() {
    companion object {
        fun serialize(documentId: Document.Id) = documentId.code
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(documentId: Document.Id, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(DocumentIdSerializer.serialize(documentId))
}