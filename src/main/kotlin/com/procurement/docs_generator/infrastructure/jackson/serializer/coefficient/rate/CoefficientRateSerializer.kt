package com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.rate

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.procurement.docs_generator.domain.model.release.entity.CoefficientRate
import java.io.IOException
import java.math.BigDecimal

class CoefficientRateSerializer : JsonSerializer<CoefficientRate>() {
    companion object {
        fun serialize(CoefficientRate: CoefficientRate): BigDecimal = CoefficientRate.rate
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(
        CoefficientRate: CoefficientRate,
        jsonGenerator: JsonGenerator,
        provider: SerializerProvider
    ) {
        val coefficient = serialize(CoefficientRate)
        if (coefficient.stripTrailingZeros().scale() == 0) {
            jsonGenerator.writeNumber(coefficient.longValueExact())
        } else {
            jsonGenerator.writeNumber(coefficient)
        }
    }
}