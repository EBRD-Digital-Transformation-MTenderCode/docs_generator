package com.procurement.docs_generator.domain.service

inline fun <reified T> TransformService.deserialize(json: String): T = this.deserialize(json, T::class.java)

interface TransformService {
    fun <T> deserialize(json: String, targetClass: Class<T>): T
    fun toMap(json: String): Map<String, Any>
    fun <T : Any> serialize(entity: T): String
}