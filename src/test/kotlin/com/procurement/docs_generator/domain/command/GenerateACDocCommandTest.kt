package com.procurement.docs_generator.domain.command

import com.procurement.docs_generator.AbstractBase
import com.procurement.docs_generator.domain.command.ac.GenerateACDocCommand
import com.procurement.docs_generator.toJson
import com.procurement.docs_generator.toObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GenerateACDocCommandTest : AbstractBase() {
    @Test
    fun test() {
        val storedJson = RESOURCES.load("json/domain/command/generate_ac_document.json")
        val snapshotData = mapper.toObject<GenerateACDocCommand>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }
}