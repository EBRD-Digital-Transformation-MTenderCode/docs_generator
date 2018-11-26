package com.procurement.docs_generator.exception.template

import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.exception.app.ApplicationException
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors
import java.time.LocalDate

class TemplateAlreadyException(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate) :
    ApplicationException(
        loglevel = Logger.Level.ERROR,
        codeError = CodesOfErrors.TEMPLATE_IS_ALREADY,
        message = "Template is already (id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}', date: '$date')."
    )
