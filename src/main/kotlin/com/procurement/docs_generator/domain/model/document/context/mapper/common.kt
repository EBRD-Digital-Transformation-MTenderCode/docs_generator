package com.procurement.docs_generator.domain.model.document.context.mapper

import com.procurement.docs_generator.domain.model.release.ACReleasesPackage

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

fun <T> ACReleasesPackage.Release.Party.mapAdditionalIdentifiersByScheme(
    scheme: String,
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

fun <T> Collection<ACReleasesPackage.Release.Party.Person.BusinessFunction>.mapBusinessFunctionByType(
    type: String,
    transformer: (ACReleasesPackage.Release.Party.Person.BusinessFunction) -> T): List<T> {
    return if (this.isEmpty())
        emptyList()
    else {
        val predicate = type.toUpperCase()
        this.asSequence()
            .filter { it.type.toUpperCase() == predicate }
            .map { identifier ->
                transformer(identifier)
            }
            .toList()
    }
}

fun <T> Collection<ACReleasesPackage.Release.Party.Person.BusinessFunction.Document>.mapDocumentsByDocumentType(
    documentType: String,
    transformer: (ACReleasesPackage.Release.Party.Person.BusinessFunction.Document) -> T): List<T> {
    return if (this.isEmpty())
        emptyList()
    else {
        val predicate = documentType.toUpperCase()
        this.asSequence()
            .filter { it.documentType.toUpperCase() == predicate }
            .map { identifier ->
                transformer(identifier)
            }
            .toList()
    }
}