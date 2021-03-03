package com.procurement.docs_generator.domain.model.release

abstract class AbstractDTOTestBase<T : Any>(private val target: Class<T>) {
    fun testBindingAndMapping(pathToJsonFile: String) {
        testingBindingAndMapping(pathToJsonFile, target)
    }
}
