package com.procurement.docs_generator.domain.model.template.engine

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import com.procurement.docs_generator.domain.model.template.Template
import java.io.IOException

class TemplateEngineDeserializer : ValueObjectDeserializer<Template.Engine>() {
    companion object {
        fun deserialize(text: String) =
            Template.Engine.valueOfCode(text)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Template.Engine =
        deserialize(
            jsonParser.text)
}