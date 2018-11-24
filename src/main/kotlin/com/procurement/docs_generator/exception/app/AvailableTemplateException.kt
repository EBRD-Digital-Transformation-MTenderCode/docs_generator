package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors

class AvailableTemplateException(document: Document) : ApplicationException(
    loglevel = Logger.Level.ERROR,
    codeError = CodesOfErrors.NO_AVAILABLE_TEMPLATE,
    message = "No template available on date: '${document.date}'."
)