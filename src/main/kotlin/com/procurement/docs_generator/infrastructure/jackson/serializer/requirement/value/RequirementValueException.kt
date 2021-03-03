package com.procurement.docs_generator.infrastructure.jackson.serializer.requirement.value

class RequirementValueException(requirementValue: String, description: String = "") :
    RuntimeException("Incorrect value in requirement: '$requirementValue'. $description")