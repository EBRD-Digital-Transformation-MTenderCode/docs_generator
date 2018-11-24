package com.procurement.docs_generator

import com.procurement.docs_generator.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackageClasses = [
        ApplicationConfiguration::class
    ]
)
class DocumentGeneratorApplication

fun main(args: Array<String>) {
    runApplication<DocumentGeneratorApplication>(*args)
}
