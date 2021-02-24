package com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.rate


class CoefficientException(coefficient: String, description: String = "") :
    RuntimeException("Incorrect coefficient: '$coefficient'. $description")
