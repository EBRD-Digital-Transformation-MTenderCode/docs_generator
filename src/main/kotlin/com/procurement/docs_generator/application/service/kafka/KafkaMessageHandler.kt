package com.procurement.docs_generator.application.service.kafka

interface KafkaMessageHandler {
    fun handle(message: String): String
}