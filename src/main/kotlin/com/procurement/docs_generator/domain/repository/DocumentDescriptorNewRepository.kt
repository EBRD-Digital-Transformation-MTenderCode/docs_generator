package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.country.Country
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.entity.DocumentDescriptorNew
import com.procurement.docs_generator.domain.model.language.Language
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

interface DocumentDescriptorNewRepository {
    fun load(
        cpid: CPID,
        ocid: OCID,
        pmd: ProcurementMethod,
        country: Country,
        lang: Language,
        documentInitiator: String
    ): DocumentDescriptorNew?

    fun save(documentDescriptor: DocumentDescriptorNew): Boolean
}