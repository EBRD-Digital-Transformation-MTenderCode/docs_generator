package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors

class MultipartFileException(message: String) : ApplicationException(
    loglevel = Logger.Level.ERROR,
    codeError = CodesOfErrors.FILE_ERROR,
    message = message
)