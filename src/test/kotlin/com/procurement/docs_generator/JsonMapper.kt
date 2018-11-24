package com.procurement.docs_generator

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException

inline fun <reified T> ObjectMapper.toObject(json: JsonNode): T = try {
    this.treeToValue(json, T::class.java)
} catch (e: IOException) {
    throw IllegalArgumentException(e)
}

inline fun <reified T> ObjectMapper.toObject(json: String): T = try {
    this.readValue(json, T::class.java)
} catch (exception: Exception) {
    throw RuntimeException(json, exception)
}

inline fun <reified T> ObjectMapper.toJson(obj: T): String = try {
    this.writeValueAsString(obj)
} catch (e: JsonProcessingException) {
    throw RuntimeException(e)
}

