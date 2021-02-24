package com.procurement.docs_generator.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notice.infrastructure.bind.jackson.configuration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfiguration{

    @Bean
    fun transform() = ObjectMapper().apply { configuration() }
}

