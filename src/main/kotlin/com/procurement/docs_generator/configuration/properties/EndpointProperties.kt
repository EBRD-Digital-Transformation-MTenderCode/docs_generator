package com.procurement.docs_generator.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "dev-endpoints")
class EndpointProperties {
    var publicPoint: String? = "http://public-point:8080"
    var storage: String? = "http://storage:8080"
}