package com.procurement.docs_generator.domain.command

import com.procurement.docs_generator.domain.model.release.AbstractDTOTestBase
import org.junit.jupiter.api.Test

class GenerateDocumentCommandTest : AbstractDTOTestBase<GenerateDocumentCommand>(GenerateDocumentCommand::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/domain/command/generate_document_command.json")
    }
}