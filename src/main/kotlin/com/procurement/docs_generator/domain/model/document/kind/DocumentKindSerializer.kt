package com.procurement.docs_generator.domain.model.document.kind

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import com.procurement.docs_generator.domain.model.document.Document
import java.io.IOException

class DocumentKindSerializer : ValueObjectSerializer<Document.Kind>() {
    companion object {
        fun serialize(documentKind: Document.Kind) = documentKind.code
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(documentKind: Document.Kind, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(DocumentKindSerializer.serialize(documentKind))
}