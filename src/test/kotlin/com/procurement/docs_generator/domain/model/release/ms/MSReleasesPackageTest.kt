package com.procurement.docs_generator.domain.model.release.ms

import com.procurement.docs_generator.AbstractBase
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import com.procurement.docs_generator.toJson
import com.procurement.docs_generator.toObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MSReleasesPackageTest : AbstractBase() {
    @Test
    fun test() {
        val storedJson = RESOURCES.load("json/domain/model/release/ms/package.json")
        val snapshotData = mapper.toObject<MSReleasesPackage>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }
}