package com.procurement.docs_generator.infrastructure.adapter

import com.procurement.docs_generator.configuration.properties.KafkaProperties
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.stereotype.Service

@Service
class KafkaConsumerFactoryImpl(
    private val kafkaProperties: KafkaProperties
) : KafkaConsumerFactory<String, String> {

    override fun create(topics: List<String>): KafkaConsumer<String, String> {
        val props = kafkaProperties.buildConsumerProperties()
        return KafkaConsumer<String, String>(props, StringDeserializer(), StringDeserializer())
            .apply {
                this.subscribe(topics)
            }
    }
}