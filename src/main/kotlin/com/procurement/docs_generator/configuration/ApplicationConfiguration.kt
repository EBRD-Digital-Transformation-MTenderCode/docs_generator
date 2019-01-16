package com.procurement.docs_generator.configuration

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    value = [
        ServiceConfiguration::class,
        KafkaConfiguration::class,
        CassandraConfiguration::class,
        ThymeleafConfiguration::class,
        WebConfiguration::class
    ]
)
@EnableAutoConfiguration(
    exclude = [
        ThymeleafAutoConfiguration::class
    ]
)
class ApplicationConfiguration
