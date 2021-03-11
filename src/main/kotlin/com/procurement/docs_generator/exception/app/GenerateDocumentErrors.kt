package com.procurement.docs_generator.exception.app

import com.procurement.docs_generator.domain.date.asString
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod
import com.procurement.docs_generator.domain.model.pmd.RecordName
import com.procurement.docs_generator.domain.model.pmd.RelatedProcessType
import com.procurement.docs_generator.infrastructure.dispatcher.CodeError
import com.procurement.docs_generator.infrastructure.dispatcher.CodesOfErrors
import java.time.LocalDateTime

sealed class GenerateDocumentErrors(code: CodeError, message: String) : ApplicationException(
    loglevel = Logger.Level.ERROR,
    codeError = code,
    message = message
) {
    class RecordNotFound(
        pmd: ProcurementMethod,
        country: Country,
        documentInitiator: String
    ) : GenerateDocumentErrors(
        code = CodesOfErrors.RECORD_NOT_FOUND,
        message = "Record not found by pmd '$pmd', country '$country' and documentInitiator '$documentInitiator'."
    )

    class RecordForParameterSearchNotFound(
        recordName: RecordName
    ) : GenerateDocumentErrors(
        code = CodesOfErrors.RECORD_NOT_FOUND,
        message = "Record '$recordName' to search parameter from is missing."
    )

    class RelationshipsNotFound(relationships: Collection<RelatedProcessType>) : GenerateDocumentErrors(
        code = CodesOfErrors.RELATIONSHIPS_NOT_FOUND,
        message = "Relationship(s) '${relationships.joinToString()}' not found in any release record."
    )

    class ValueByPathNotFound(path: String) : GenerateDocumentErrors(
        code = CodesOfErrors.VALUE_BY_PATH_NOT_FOUND,
        message = "Parameter value by path '$path' not found."
    )

    class NoTemplateFound(
        pmd: ProcurementMethod,
        country: Country,
        documentInitiator: String,
        language: Language,
        subGroup: String
    ) : GenerateDocumentErrors(
        code = CodesOfErrors.TEMPLATE_NOT_FOUND,
        message = "No template found by country '${country}', documentInitiator '${documentInitiator}', " +
            "pmd '${pmd}', lang '{$language}' and subGroup '$subGroup'."
    )

    class NoTemplateEqualsOrPrecedesSpecifiedDate(
        pmd: ProcurementMethod,
        country: Country,
        documentInitiator: String,
        language: Language,
        subGroup: String,
        date: LocalDateTime
    ): GenerateDocumentErrors(
        code = CodesOfErrors.TEMPLATE_NOT_FOUND,
        message = "No template with country '${country}', documentInitiator '${documentInitiator}', " +
            "pmd '${pmd}', lang '{$language}' and subGroup '$subGroup' is equal or precedes date '${date.asString()}'."
    )

    class RelationshipIsNotAllowed(relationship: RelatedProcessType) : GenerateDocumentErrors(
        code = CodesOfErrors.RELATIONSHIP_IS_NOT_ALLOWED,
        message = "Relationship '$relationship' is not allowed"
    )
}