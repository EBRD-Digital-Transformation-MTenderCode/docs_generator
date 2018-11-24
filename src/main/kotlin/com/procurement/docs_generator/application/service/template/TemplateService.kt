package com.procurement.docs_generator.application.service.template

import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

interface TemplateService {
    fun add(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate, file: MultipartFile)

    fun update(id: Document.Id, kind: Document.Kind, lang: Language, date: LocalDate, file: MultipartFile)
}

