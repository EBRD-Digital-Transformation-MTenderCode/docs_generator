package com.procurement.docs_generator.exception.database

class SaveOperationException : RuntimeException {
    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(message: String) : super(message)
}