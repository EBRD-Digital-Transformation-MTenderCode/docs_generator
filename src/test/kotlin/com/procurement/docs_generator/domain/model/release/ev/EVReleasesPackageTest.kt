package com.procurement.docs_generator.domain.model.release.ev

import com.procurement.docs_generator.AbstractBase
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.toJson
import com.procurement.docs_generator.toObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EVReleasesPackageTest : AbstractBase() {
    @Test
    fun test() {
        val storedJson = RESOURCES.load("json/domain/model/release/ev/package.json")
        val snapshotData = mapper.toObject<EVReleasesPackage>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }
}