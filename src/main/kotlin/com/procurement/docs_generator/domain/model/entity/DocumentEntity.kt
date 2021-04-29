package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

data class DocumentEntity(
    val cpid: CPID,
    val ocid: OCID,
    val documents: List<Document>,
    val pmd: ProcurementMethod,
    val country: Country,
    val lang: Language,
    val processInitiator: String,
    val objectId: String
) {
    data class Document(val id: String)
}