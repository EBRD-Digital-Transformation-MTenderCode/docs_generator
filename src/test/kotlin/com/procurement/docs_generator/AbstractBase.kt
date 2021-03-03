package com.procurement.docs_generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notice.infrastructure.bind.jackson.configuration

abstract class AbstractBase {
    companion object {
        val mapper = ObjectMapper().apply { configuration() }

        val RESOURCES = JsonResource()
    }
}
