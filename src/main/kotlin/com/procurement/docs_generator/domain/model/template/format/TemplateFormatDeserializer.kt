package com.procurement.docs_generator.domain.model.template.format

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.procurement.docs_generator.domain.model.ValueObjectDeserializer
import com.procurement.docs_generator.domain.model.template.Template
import java.io.IOException

class TemplateFormatDeserializer : ValueObjectDeserializer<Template.Format>() {
    companion object {
        fun deserialize(text: String) = Template.Format.valueOfCode(text)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Template.Format =
        deserialize(jsonParser.text)
}