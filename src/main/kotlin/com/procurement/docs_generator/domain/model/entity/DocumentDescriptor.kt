package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.command.id.CommandId
import com.procurement.docs_generator.domain.model.document.Document
import com.procurement.docs_generator.domain.model.language.Language

class DocumentDescriptor(
    val commandId: CommandId,
    val documentId: Document.Id,
    val documentKind: Document.Kind,
    val lang: Language,
    val descriptor: String
)