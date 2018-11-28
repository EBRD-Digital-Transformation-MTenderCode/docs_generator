package com.procurement.docs_generator.configuration.properties

import com.procurement.docs_generator.domain.model.version.ApiVersion

object GlobalProperties {
    const val serviceId = "17"

    object App {
        val apiVersion = ApiVersion(1, 0, 0)
    }
}

