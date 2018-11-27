package com.procurement.docs_generator.domain.model.document.context.mapper

import com.procurement.docs_generator.domain.model.date.JsonDateTimeDeserializer
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

fun List<ACReleasesPackage.Release.Party>.partyByRole(role: String): ACReleasesPackage.Release.Party {
    if (this.isEmpty())
        throw IllegalArgumentException("Collection of the parties is empty.")

    val predicate = role.toUpperCase()
    for (party in this) {
        val found = party.roles.any {
            it.toUpperCase() == predicate
        }

        if (found) return party
    }

    throw IllegalStateException("Party by role: '$role' not found.")
}

fun <T> ACReleasesPackage.Release.Party.mapAdditionalIdentifiersByScheme(scheme: String,
                                                                         transformer: (ACReleasesPackage.Release.Party.AdditionalIdentifier) -> T): List<T> {
    return if (this.additionalIdentifiers == null)
        emptyList()
    else {
        val predicate = scheme.toUpperCase()
        this.additionalIdentifiers.asSequence()
            .filter { it.scheme.toUpperCase() == predicate }
            .map { identifier ->
                transformer(identifier)
            }
            .toList()
    }
}

fun formattingDate(date: String): String {
    return JsonDateTimeDeserializer.deserialize(date).toLocalDate().format(dateFormatter)
}

fun formattingDate(date: LocalDate): String {
    return date.format(dateFormatter)
}