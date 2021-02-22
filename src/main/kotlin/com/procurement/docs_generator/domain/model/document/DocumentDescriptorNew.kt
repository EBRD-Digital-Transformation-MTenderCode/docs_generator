package com.procurement.docs_generator.domain.model.document

import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.pmd.ProcurementMethod

class DocumentDescriptorNew(
    val cpid: CPID,
    val ocid: OCID,
    val pmd: ProcurementMethod,
    val country: String,
    val lang: String,
    val documentInitiator: String,
    val descriptor: String
)