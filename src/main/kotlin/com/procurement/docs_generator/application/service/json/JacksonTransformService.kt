package com.procurement.docs_generator.application.service.json

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.procurement.docs_generator.domain.service.TransformService
import com.procurement.docs_generator.exception.json.JsonParseToObjectException
import org.springframework.stereotype.Service
import java.util.*

@Service
class JacksonTransformService(private val objectMapper: ObjectMapper) : TransformService {
    companion object {
        private val typeRefOfMap = object : TypeReference<HashMap<String, Any>>() {}
    }

    override fun <T> deserialize(json: String, targetClass: Class<T>): T = try {
        objectMapper.readValue(json, targetClass)
    } catch (exception: Exception) {
        throw JsonParseToObjectException(exception)
    }

    override fun <T> deserializeCollection(json: String, elementClass: Class<T>): List<T> = try {
        val javaType: CollectionType = objectMapper.typeFactory
            .constructCollectionType(List::class.java, elementClass)
        objectMapper.readValue(json, javaType)
    } catch (exception: Exception) {
        throw JsonParseToObjectException(exception)
    }

    override fun toMap(json: String): Map<String, Any> = try {
        objectMapper.readValue(json, typeRefOfMap)
    } catch (exception: Exception) {
        throw JsonParseToObjectException(exception)
    }

    override fun <T> serialize(entity: T): String = try {
        objectMapper.writeValueAsString(entity)
    } catch (expected: JsonProcessingException) {
        val className = this::class.java.canonicalName
        throw IllegalArgumentException("Error mapping an object of type '$className' to JSON.", expected)
    }

    override fun <R> toJsonNode(value: R): JsonNode = try {
        objectMapper.valueToTree(value)
    } catch (expected: Exception) {
        val className = this::class.java.canonicalName
        throw IllegalArgumentException("Error mapping an object of type '$className' to Json Node.", expected)
    }
}