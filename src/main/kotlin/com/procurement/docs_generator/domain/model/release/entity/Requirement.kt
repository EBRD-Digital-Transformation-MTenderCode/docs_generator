package com.procurement.docs_generator.domain.model.release.entity

import java.math.BigDecimal
import java.time.LocalDateTime

class Requirement(
    val id: String,
    val title: String,
    val description: String?,
    val period: Period?,
    val status: String?,
    val datePublished: LocalDateTime?,
    val dataType: RequirementDataType?,
    val value: RequirementValue,
    val eligibleEvidences: List<EligibleEvidence>?
) {
    data class Period(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime
    )

    data class EligibleEvidence(
        val id: String,
        val title: String,
        val type: String,
        val description: String?,
        val relatedDocument: RelatedDocument?
    ) {
        data class RelatedDocument(
            val id: String
        )
    }
}

sealed class RequirementValue

sealed class ExpectedValue : RequirementValue() {
    companion object {
        fun of(value: Boolean) = AsBoolean(value)
        fun of(value: String) = AsString(value)
        fun of(value: BigDecimal) = AsNumber(value)
        fun of(value: Long) = AsInteger(value)
    }

    data class AsBoolean(val value: Boolean) : ExpectedValue()
    data class AsString(val value: String) : ExpectedValue()
    data class AsNumber(val value: BigDecimal) : ExpectedValue()
    data class AsInteger(val value: Long) : ExpectedValue()
}

sealed class MinValue : RequirementValue() {
    companion object {
        fun of(value: BigDecimal) = AsNumber(value)
        fun of(value: Long) = AsInteger(value)
    }

    data class AsNumber(val value: BigDecimal) : MinValue()
    data class AsInteger(val value: Long) : MinValue()
}

sealed class MaxValue : RequirementValue() {
    companion object {
        fun of(value: BigDecimal) = AsNumber(value)
        fun of(value: Long) = AsInteger(value)
    }

    data class AsNumber(val value: BigDecimal) : MaxValue()
    data class AsInteger(val value: Long) : MaxValue()
}

sealed class RangeValue : RequirementValue() {
    companion object {
        fun of(minValue: BigDecimal, maxValue: BigDecimal) =
            AsNumber(
                minValue = minValue,
                maxValue = maxValue
            )

        fun of(minValue: Long, maxValue: Long) =
            AsInteger(
                minValue = minValue,
                maxValue = maxValue
            )
    }

    data class AsNumber(val minValue: BigDecimal, val maxValue: BigDecimal) : RangeValue()
    data class AsInteger(val minValue: Long, val maxValue: Long) : RangeValue()
}

object NoneValue : RequirementValue()
