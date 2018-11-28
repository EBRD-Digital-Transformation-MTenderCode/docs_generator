package com.procurement.docs_generator.application.service.json

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.docs_generator.domain.service.JsonDeserializeService
import com.procurement.docs_generator.exception.json.JsonParseToObjectException
import org.springframework.stereotype.Service
import java.util.*

@Service
class JacksonJsonDeserializeService(private val objectMapper: ObjectMapper) : JsonDeserializeService {
    companion object {
        private val typeRefOfMap = object : TypeReference<HashMap<String, Any>>() {}
    }

    override fun <T> deserialize(json: String, targetClass: Class<T>): T = try {
        objectMapper.readValue(json, targetClass)
    } catch (exception: Exception) {
        throw JsonParseToObjectException(exception)
    }

    override fun toMap(json: String): Map<String, Any> = try {
        objectMapper.readValue(json, typeRefOfMap)
    } catch (exception: Exception) {
        throw JsonParseToObjectException(exception)
    }
}