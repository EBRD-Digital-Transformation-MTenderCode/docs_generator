package com.procurement.docs_generator.application.service.template

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.template.Template
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

interface TemplateService {
    fun add(id: Document.Id,
            kind: Document.Kind,
            lang: Language,
            date: LocalDate,
            file: MultipartFile,
            format: Template.Format,
            engine: Template.Engine)

    fun update(id: Document.Id,
               kind: Document.Kind,
               lang: Language,
               date: LocalDate,
               file: MultipartFile,
               format: Template.Format,
               engine: Template.Engine)

    fun getAvailableTemplate(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate): Template

    fun add(
        country: Country,
        pmd: ProcurementMethod,
        processInitiator: String,
        lang: Language,
        subGroup: String,
        date: LocalDate,
        format: Template.Format,
        engine: Template.Engine,
        file: MultipartFile
    )
}