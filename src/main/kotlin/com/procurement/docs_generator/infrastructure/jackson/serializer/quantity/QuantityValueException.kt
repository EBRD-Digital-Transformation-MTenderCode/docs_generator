package com.procurement.docs_generator.infrastructure.jackson.serializer.quantity

class QuantityValueException(quantity: String, description: String = "") :
    RuntimeException("Incorrect value of the quantity: '$quantity'. $description")