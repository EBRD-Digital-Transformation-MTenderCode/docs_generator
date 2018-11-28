package com.procurement.docs_generator.domain.date

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class JsonDateDeserializer : JsonDeserializer<LocalDate>() {
    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .toFormatter()

        fun deserialize(dateTime: String): LocalDate = LocalDate.parse(dateTime, formatter)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDate {
        val dateTime = jsonParser.text
        return deserialize(dateTime)
    }
}