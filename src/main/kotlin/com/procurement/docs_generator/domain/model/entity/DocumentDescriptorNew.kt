package com.procurement.docs_generator.domain.model.entity

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

data class DocumentDescriptorNew(
    val cpid: CPID,
    val ocid: OCID,
    val documents: Documents,
    val pmd: ProcurementMethod,
    val country: Country,
    val lang: Language,
    val documentInitiator: String,
    val objectId: String
) {
    class Documents(values: List<Document>) : List<Documents.Document> by values {
        data class Document(val id: String)
    }
}