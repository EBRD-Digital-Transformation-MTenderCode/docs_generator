package com.procurement.docs_generator.configuration

import com.procurement.docs_generator.configuration.properties.EndpointProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
@EnableConfigurationProperties(
    value = [
        EndpointProperties::class
    ]
)
@ComponentScan(
    basePackages = [
        "com.procurement.docs_generator.domain.service",
        "com.procurement.docs_generator.application.service",
        "com.procurement.docs_generator.infrastructure.adapter",
        "com.procurement.docs_generator.infrastructure.dispatcher",
        "com.procurement.docs_generator.infrastructure.service"
    ]
)
class ServiceConfiguration {
    @Bean
    fun webClient(): RestTemplate = RestTemplate()
}
