package com.procurement.docs_generator.domain.model

import com.fasterxml.jackson.databind.JsonSerializer

abstract class ValueObjectSerializer<T : ValueObject> : JsonSerializer<T>()