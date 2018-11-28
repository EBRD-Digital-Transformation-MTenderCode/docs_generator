package com.procurement.docs_generator.infrastructure.adapter

import org.springframework.restdocs.constraints.ConstraintDescriptions
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.snippet.Attributes.key
import java.util.*

object ModelDescription {
    object AddTemplate {
        fun responseSuccessful(): List<FieldDescriptor> {
            return listOf(
                getFieldDescriptor("data", "The data of response.")
            )
        }
    }

    object UpdateTemplate {
        fun responseSuccessful(): List<FieldDescriptor> {
            return listOf(
                getFieldDescriptor("data", "The data of response.")
            )
        }
    }

    fun responseError(): List<FieldDescriptor> {
        return listOf(
            getFieldDescriptor("errors", "List of errors."),
            getFieldDescriptor("errors[].code", "The code of the error."),
            getFieldDescriptor("errors[].description", "The description of the error.")
        )
    }
}

private fun getFieldDescriptor(
    property: String,
    description: String,
    constraint: ConstraintDescriptions
): FieldDescriptor {
    return fieldWithPath(property)
        .description(formattingDescription(description))
        .attributes(
            key("constraints")
                .value(getConstraints(constraint, property))
        )
}

private fun getFieldDescriptor(property: String, description: String): FieldDescriptor {
    return fieldWithPath(property).description(description)
}

private fun formattingDescription(description: String): String {
    val text = description.trim { it <= ' ' }
    return if (text.endsWith(".")) text else "$text."
}

private fun getConstraints(constraint: ConstraintDescriptions, property: String): String {
    val descriptions = constraint.descriptionsForProperty(property)
    return if (descriptions.isEmpty()) {
        ""
    } else {
        val stringJoiner = StringJoiner(".\n", "", ".")
        descriptions.forEach { stringJoiner.add(it) }
        stringJoiner.toString()
    }
}