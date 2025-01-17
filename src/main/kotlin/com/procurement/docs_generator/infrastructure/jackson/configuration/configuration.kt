package com.procurement.notice.infrastructure.bind.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.procurement.docs_generator.domain.model.date.JsonDateTimeDeserializer
import com.procurement.docs_generator.domain.model.date.JsonDateTimeSerializer
import com.procurement.docs_generator.domain.model.release.entity.CoefficientRate
import com.procurement.docs_generator.domain.model.release.entity.CoefficientValue
import com.procurement.docs_generator.domain.model.release.entity.RequirementRsValue
import com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.rate.CoefficientRateDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.rate.CoefficientRateSerializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.value.CoefficientValueDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.value.CoefficientValueSerializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.value.RequirementValueDeserializer
import com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.value.RequirementValueSerializer
import java.time.LocalDateTime

fun ObjectMapper.configuration() {
    val module = SimpleModule().apply {
        /**
         * Serializer/Deserializer for LocalDateTime type
         */
        addSerializer(LocalDateTime::class.java, JsonDateTimeSerializer())
        addDeserializer(LocalDateTime::class.java, JsonDateTimeDeserializer())

        addSerializer(CoefficientRate::class.java, CoefficientRateSerializer())
        addDeserializer(CoefficientRate::class.java, CoefficientRateDeserializer())

        addSerializer(CoefficientValue::class.java, CoefficientValueSerializer())
        addDeserializer(CoefficientValue::class.java, CoefficientValueDeserializer())

        addSerializer(RequirementRsValue::class.java, RequirementValueSerializer())
        addDeserializer(RequirementRsValue::class.java, RequirementValueDeserializer())
    }

    this.registerModule(module)
    this.registerModule(KotlinModule())
    this.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
    this.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
    this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    this.nodeFactory = JsonNodeFactory.withExactBigDecimals(true)
}
