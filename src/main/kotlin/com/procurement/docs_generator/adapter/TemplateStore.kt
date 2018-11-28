package com.procurement.docs_generator.adapter

import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.template.Template
import java.nio.ByteBuffer
import java.time.LocalDate

interface TemplateStore {
    fun getTemplateDates(id: Document.Id,
                         kind: Document.Kind,
                         lang: Language): List<LocalDate>

    fun getTemplate(id: Document.Id,
                    kind: Document.Kind,
                    lang: Language,
                    date: LocalDate): Template

    fun add(id: Document.Id,
            kind: Document.Kind,
            lang: Language,
            date: LocalDate,
            format: Template.Format,
            engine: Template.Engine,
            body: ByteBuffer)

    fun update(id: Document.Id,
               kind: Document.Kind,
               lang: Language,
               date: LocalDate,
               format: Template.Format,
               engine: Template.Engine,
               body: ByteBuffer)
}