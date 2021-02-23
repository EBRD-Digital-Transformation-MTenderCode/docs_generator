package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.command.id.CommandId
import com.procurement.docs_generator.domain.model.entity.DocumentDescriptor

interface DocumentDescriptorRepository {
    fun load(commandId: CommandId): DocumentDescriptor?
    fun save(documentDescriptor: DocumentDescriptor): DocumentDescriptor
}