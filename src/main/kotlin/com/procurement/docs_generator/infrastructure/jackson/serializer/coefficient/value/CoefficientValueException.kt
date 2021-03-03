package com.procurement.docs_generator.infrastructure.jackson.serializer.coefficient.value

class CoefficientValueException(coefficientValue: String, description: String = "") :
    RuntimeException("Incorrect value of the coefficient: '$coefficientValue'. $description")
