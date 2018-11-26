package com.procurement.docs_generator.domain.model.template.format

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import com.procurement.docs_generator.domain.model.template.Template
import java.io.IOException

class TemplateFormatSerializer : ValueObjectSerializer<Template.Format>() {
    companion object {
        fun serialize(mode: Template.Format) = mode.code
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(format: Template.Format, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(serialize(
            format))
}