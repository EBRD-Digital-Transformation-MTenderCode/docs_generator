package com.procurement.docs_generator.domain.model.document

import com.procurement.docs_generator.domain.model.language.Language
import org.thymeleaf.context.IContext
import java.time.LocalDate

class AwardContract(kind: Document.Kind, date: LocalDate, lang: Language, context: IContext) :
    Document(
        id = Document.Id.AWARD_CONTRACT,
        kind = kind,
        date = date,
        lang = lang,
        context = context
    )