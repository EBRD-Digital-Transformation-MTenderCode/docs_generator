package com.procurement.docs_generator.exception.template

import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import java.time.LocalDate

class TemplateNotFoundException(id: Document.Id,
                                kind: Document.Kind,
                                lang: Language,
                                date: LocalDate? = null) :
    RuntimeException("Template by id: '${id.code}', kind: '${kind.code}', lang: '${lang.value}' ${if (date != null) ", date: '$date'" else ""}  not found.")
