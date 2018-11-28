package com.procurement.docs_generator.domain.date

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class JsonDateSerializer : JsonSerializer<LocalDate>() {
    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .toFormatter()

        fun serialize(data: LocalDate): String = data.format(formatter)
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(date: LocalDate, jsonGenerator: JsonGenerator, provider: SerializerProvider) {
        jsonGenerator.writeString(serialize(date))
    }
}