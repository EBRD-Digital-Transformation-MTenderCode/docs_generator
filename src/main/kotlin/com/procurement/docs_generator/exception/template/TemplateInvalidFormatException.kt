package com.procurement.docs_generator.exception.template

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.exception.app.ApplicationException
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors

class TemplateInvalidFormatException(exception: Exception? = null) :
    ApplicationException(
        loglevel = Logger.Level.ERROR,
        codeError = CodesOfErrors.TEMPLATE_INVALID_FORMAT,
        message = "Invalid format of the template.",
        cause = exception
    )
