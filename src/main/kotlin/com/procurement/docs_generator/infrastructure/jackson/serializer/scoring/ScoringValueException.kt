package com.procurement.docs_generator.infrastructure.jackson.serializer.scoring

class ScoringValueException(scoring: String, description: String = "") :
    RuntimeException("Incorrect value of the scoring: '$scoring'. $description")