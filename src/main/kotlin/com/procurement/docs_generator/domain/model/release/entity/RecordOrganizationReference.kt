package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.address.RecordAddress
import com.procurement.docs_generator.domain.model.release.entity.parties.RecordDetails
import com.procurement.docs_generator.domain.model.release.entity.parties.RecordPerson

data class RecordOrganizationReference(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RecordIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("address") @param:JsonProperty("address") val address: RecordAddress?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<RecordIdentifier> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: RecordContactPoint?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: RecordDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("buyerProfile") @param:JsonProperty("buyerProfile") val buyerProfile: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<RecordPerson> = emptyList()
)
