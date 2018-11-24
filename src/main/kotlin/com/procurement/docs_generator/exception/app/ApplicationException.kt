package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.infrastructure.dispatcher.CodeError

abstract class ApplicationException(val loglevel: Logger.Level,
                                    val codeError: CodeError,
                                    message: String,
                                    cause: Throwable? = null) :
    RuntimeException(message, cause)