package com.procurement.docs_generator.adapter

import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import com.procurement.docs_generator.domain.model.release.entity.RecordPackage

interface PublicPointAdapter {
    fun getACReleasePackage(cpid: CPID, ocid: OCID): ACReleasesPackage

    fun getEVReleasePackage(cpid: CPID, ocid: OCID): EVReleasesPackage

    fun getMSReleasePackage(cpid: CPID): MSReleasesPackage

    fun getReleasePackage(cpid: CPID, ocid: OCID): RecordPackage
}


