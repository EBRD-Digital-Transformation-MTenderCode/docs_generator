package com.procurement.docs_generator.domain.service

interface JsonSerializeService {
    fun <T> serialize(obj: T): String
}