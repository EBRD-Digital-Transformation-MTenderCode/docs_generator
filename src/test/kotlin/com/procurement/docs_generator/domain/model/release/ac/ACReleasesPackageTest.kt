package com.procurement.docs_generator.domain.model.release.ac

import com.procurement.docs_generator.AbstractBase
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.toJson
import com.procurement.docs_generator.toObject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ACReleasesPackageTest : AbstractBase() {
    @Test
    fun test() {
        val storedJson = RESOURCES.load("json/domain/model/release/ac/package_with_all_attributes.json")
        val snapshotData = mapper.toObject<ACReleasesPackage>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }

    @Test
    fun testOnlyRequire1() {
        val storedJson = RESOURCES.load("json/domain/model/release/ac/package_without_optional_attributes_1.json")
        val snapshotData = mapper.toObject<ACReleasesPackage>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }

    @Test
    fun testOnlyRequire2() {
        val storedJson = RESOURCES.load("json/domain/model/release/ac/package_without_optional_attributes_2.json")
        val snapshotData = mapper.toObject<ACReleasesPackage>(storedJson)
        Assertions.assertNotNull(snapshotData)

        val jsonFromObj = mapper.toJson(snapshotData)
        Assertions.assertEquals(storedJson, jsonFromObj)
    }
}