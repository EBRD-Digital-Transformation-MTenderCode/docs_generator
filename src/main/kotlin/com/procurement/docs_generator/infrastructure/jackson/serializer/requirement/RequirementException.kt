package com.procurement.docs_generator.infrastructure.jackson.serializer.requirement

class RequirementException(message: String = "") :
    RuntimeException("Incorrect requirement value. $message")