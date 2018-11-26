package com.procurement.docs_generator.domain.command

import com.procurement.docs_generator.AbstractBase
import com.procurement.docs_generator.domain.command.ac.ContractFinalizationCommand
import com.procurement.docs_generator.toJson
import com.procurement.docs_generator.toObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ContractFinalizationCommandTest : AbstractBase() {
    @Test
    fun test() {
        val storedJson = RESOURCES.load("json/domain/command/contract_finalization.json")
        val snapshotData = mapper.toObject<ContractFinalizationCommand>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }
}