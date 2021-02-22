package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.document.DocumentDescriptorNew
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

interface DocumentDescriptorNewRepository {
    fun load(
        cpid: CPID,
        ocid: OCID,
        pmd: ProcurementMethod,
        country: String,
        lang: String,
        documentInitiator: String
    ): DocumentDescriptorNew?

    fun save(documentDescriptor: DocumentDescriptorNew): Boolean
}