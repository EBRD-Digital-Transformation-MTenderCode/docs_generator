package com.procurement.docs_generator.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "external-endpoints")
data class EndpointProperties(
    var publicPoint: String? = null,
    var storage: Storage? = null

) {
    data class Storage(
        var registration: String? = null,
        var upload: String? = null
    )
}