package com.procurement.docs_generator.infrastructure.adapter

import com.procurement.docs_generator.adapter.PublicPointAdapter
import com.procurement.docs_generator.adapter.RemoteClient
import com.procurement.docs_generator.adapter.get
import com.procurement.docs_generator.configuration.properties.EndpointProperties
import com.procurement.docs_generator.domain.logger.Logger
import com.procurement.docs_generator.domain.logger.debug
import com.procurement.docs_generator.domain.model.cpid.CPID
import com.procurement.docs_generator.domain.model.ocid.OCID
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import com.procurement.docs_generator.infrastructure.logger.Slf4jLogger
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder

@Service
class RestPublicPointAdapter(
    private val endpointProperties: EndpointProperties,
    private val remoteClient: RemoteClient
) : PublicPointAdapter {

    companion object {
        private val log: Logger = Slf4jLogger()
    }

    private val PublicPointDomain = endpointProperties.publicPoint!!

    override fun getACReleasePackage(cpid: CPID, ocid: OCID): ACReleasesPackage {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug { "The request to Public-Point (AC release package): '${uri.toURL()}'" }
        return remoteClient.get(uri)
    }

    override fun getEVReleasePackage(cpid: CPID, ocid: OCID): EVReleasesPackage {
        val uri = genTendersUri(cpid = cpid, ocid = ocid)
        log.debug { "The request to Public-Point (EV release package): '${uri.toURL()}'" }
        return remoteClient.get(uri)
    }

    override fun getMSReleasePackage(cpid: CPID): MSReleasesPackage {
        val uri = genTendersUri(cpid = cpid)
        log.debug { "The request to Public-Point (MS release package): '${uri.toURL()}'" }
        return remoteClient.get(uri)
    }

    private fun genTendersUri(cpid: CPID) = UriComponentsBuilder.fromHttpUrl(PublicPointDomain)
        .pathSegment("tenders")
        .pathSegment(cpid.value)
        .pathSegment(cpid.value)
        .build(emptyMap<String, Any>())

    private fun genTendersUri(cpid: CPID, ocid: OCID) =
        UriComponentsBuilder.fromHttpUrl(endpointProperties.publicPoint!!)
            .pathSegment("tenders")
            .pathSegment(cpid.value)
            .pathSegment(ocid.value)
            .build(emptyMap<String, Any>())
}