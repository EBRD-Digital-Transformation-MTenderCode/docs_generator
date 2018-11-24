package com.procurement.docs_generator.infrastructure.adapter

import org.apache.kafka.clients.consumer.KafkaConsumer

interface KafkaConsumerFactory<K, V> {
    fun create(topics: List<String>): KafkaConsumer<K, V>
}