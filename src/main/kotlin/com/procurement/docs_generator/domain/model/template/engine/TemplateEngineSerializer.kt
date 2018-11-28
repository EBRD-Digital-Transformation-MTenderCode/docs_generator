package com.procurement.docs_generator.domain.model.template.engine

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.ValueObjectSerializer
import com.procurement.docs_generator.domain.model.template.Template
import java.io.IOException

class TemplateEngineSerializer : ValueObjectSerializer<Template.Engine>() {
    companion object {
        fun serialize(engine: Template.Engine) = engine.code
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(engine: Template.Engine, jsonGenerator: JsonGenerator, provider: SerializerProvider) =
        jsonGenerator.writeString(serialize(engine))
}