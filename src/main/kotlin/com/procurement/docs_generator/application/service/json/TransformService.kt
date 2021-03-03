package com.procurement.docs_generator.application.service.json

import com.fasterxml.jackson.databind.JsonNode

inline fun <reified T> TransformService.deserialize(json: String): T = this.deserialize(json, T::class.java)

interface TransformService {
    fun <T> deserialize(json: String, targetClass: Class<T>): T
    fun <T> deserializeCollection(json: String, elementClass: Class<T>): List<T>
    fun toMap(json: String): Map<String, Any>
    fun <T> serialize(entity: T): String
    fun <R> toJsonNode(value: R): JsonNode
}