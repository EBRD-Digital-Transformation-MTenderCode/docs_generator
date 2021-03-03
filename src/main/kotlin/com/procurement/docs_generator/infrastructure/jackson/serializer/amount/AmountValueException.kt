package com.procurement.docs_generator.infrastructure.jackson.serializer.amount

class AmountValueException(amount: String, description: String = "") :
    RuntimeException("Incorrect value of the amount: '$amount'. $description")