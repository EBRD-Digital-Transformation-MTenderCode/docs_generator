package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors
import java.time.LocalDate

class AvailableTemplateException(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate) :
    ApplicationException(
        loglevel = Logger.Level.ERROR,
        codeError = CodesOfErrors.NO_AVAILABLE_TEMPLATE,
        message = "No available template  by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date'."
    )