package com.procurement.docs_generator.exception.json

class JsonParseToObjectException(exception: Throwable)
    : RuntimeException("Error of parsing JSON.\n${exception.message}", exception)