package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors

class InvalidValueOfParamException(nameAndTypeParam: String, valueParam: String) : ApplicationException(
    loglevel = Logger.Level.ERROR,
    codeError = CodesOfErrors.INVALID_VALUE_OF_PARAM,
    message = "Invalid value: '$valueParam' of '$nameAndTypeParam'."
)