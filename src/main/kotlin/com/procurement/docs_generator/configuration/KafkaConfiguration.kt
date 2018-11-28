package com.procurement.docs_generator.configuration

import com.procurement.docs_generator.configuration.properties.KafkaProperties
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * The Java-configuration of Services.
 */
@Configuration
@EnableConfigurationProperties(
    value = [
        KafkaProperties::class
    ]
)
class KafkaConfiguration @Autowired constructor(
    private val kafkaProperties: KafkaProperties
) {

    @Bean
    fun kafkaProducer(): KafkaProducer<String, String> {
        val props = kafkaProperties.buildProducerProperties()
        return KafkaProducer<String, String>(props, StringSerializer(), StringSerializer())
    }
}