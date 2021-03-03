package com.procurement.docs_generator.domain.model.release.record

import com.procurement.docs_generator.domain.model.release.AbstractDTOTestBase
import com.procurement.docs_generator.domain.model.release.entity.RecordPackage
import org.junit.jupiter.api.Test

class RecordPackageTest : AbstractDTOTestBase<RecordPackage>(RecordPackage::class.java) {

    @Test
    fun fully() {
        testBindingAndMapping("json/domain/model/release/record/package.json")
    }
}