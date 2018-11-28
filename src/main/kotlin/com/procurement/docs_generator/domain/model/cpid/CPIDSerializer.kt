package com.procurement.docs_generator.domain.model.cpid

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import java.io.IOException

class CPIDSerializer : ValueObjectSerializer<CPID>() {
    companion object {
        fun serialize(cpid: CPID) = cpid.value
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(cpid: CPID, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(CPIDSerializer.serialize(cpid))
}