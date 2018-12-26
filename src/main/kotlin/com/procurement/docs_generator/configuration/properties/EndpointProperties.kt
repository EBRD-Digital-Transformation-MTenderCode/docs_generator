package com.procurement.docs_generator.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "external-endpoints")
class EndpointProperties {
    var publicPoint: String? = null
    var storage: String? = null
}