package com.procurement.docs_generator.application.service.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.docs_generator.domain.service.JsonSerializeService
import org.springframework.stereotype.Service

@Service
class JacksonJsonSerializeService(private val objectMapper: ObjectMapper) : JsonSerializeService {
    override fun <T> serialize(obj: T): String = try {
        objectMapper.writeValueAsString(obj)
    } catch (e: JsonProcessingException) {
        throw RuntimeException(e)
    }
}