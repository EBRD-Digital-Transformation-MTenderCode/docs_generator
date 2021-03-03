package com.procurement.docs_generator.application.service.document

import com.procurement.docs_generator.domain.command.GenerateDocumentCommand
import com.procurement.docs_generator.domain.command.GenerateDocumentResponse
import com.procurement.docs_generator.domain.command.ac.ContractFinalizationCommand
import com.procurement.docs_generator.domain.command.ac.GenerateACDocCommand

interface DocumentService {
    fun processing(command: GenerateACDocCommand): ContractFinalizationCommand.Data
    fun processing(command: GenerateDocumentCommand): GenerateDocumentResponse.Data

}