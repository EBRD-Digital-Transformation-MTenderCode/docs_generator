package com.procurement.docs_generator.domain.model.ocid

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import java.io.IOException

class OCIDSerializer : ValueObjectSerializer<OCID>() {
    companion object {
        fun serialize(ocid: OCID) = ocid.value
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(ocid: OCID, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(OCIDSerializer.serialize(ocid))
}