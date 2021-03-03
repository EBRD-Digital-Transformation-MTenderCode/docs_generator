package com.procurement.docs_generator.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.docs_generator.application.service.json.TransformService
import com.procurement.docs_generator.infrastructure.jackson.transform.JacksonTransformService
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TransformConfiguration {

    @Bean
    fun transformService(): TransformService =
        JacksonTransformService(objectMapper = ObjectMapper().apply { configuration() })
}
