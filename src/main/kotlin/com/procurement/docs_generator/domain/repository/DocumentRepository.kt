package com.procurement.docs_generator.domain.repository

import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.entity.DocumentEntity
import com.procurement.docs_generator.domain.model.ocid.OCID

interface DocumentRepository {
    fun load(
        cpid: CPID,
        ocid: OCID,
        documentInitiator: String,
        objectId: String
    ): DocumentEntity?

    fun save(documentDescriptor: DocumentEntity): Boolean
}