package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.bids.RecordBids
import com.procurement.docs_generator.domain.model.release.entity.contracts.RecordContract
import com.procurement.docs_generator.domain.model.release.entity.invitation.RecordInvitation
import com.procurement.docs_generator.domain.model.release.entity.parties.RecordOrganization
import com.procurement.docs_generator.domain.model.release.entity.planning.RecordPlanning
import com.procurement.docs_generator.domain.model.release.entity.qualification.RecordQualification
import com.procurement.docs_generator.domain.model.release.entity.submission.RecordSubmission
import com.procurement.docs_generator.domain.model.release.entity.tender.RecordTender
import com.procurement.notice.infrastructure.dto.enObservationtity.awards.RecordAward
import java.time.LocalDateTime

data class Record(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("tag") @param:JsonProperty("tag") val tag: List<Tag> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("initiationType") @param:JsonProperty("initiationType") val initiationType: InitiationType?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: RecordPlanning?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("parties") @param:JsonProperty("parties") val parties: MutableList<RecordOrganization> = mutableListOf(),

    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: RecordTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<RecordAward> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: RecordBids?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("contracts") @param:JsonProperty("contracts") val contracts: List<RecordContract> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("submissions") @param:JsonProperty("submissions") val submissions: RecordSubmission?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("qualifications") @param:JsonProperty("qualifications") val qualifications: List<RecordQualification> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<RecordAgreedMetric> = emptyList(),

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasPreviousNotice") @param:JsonProperty("hasPreviousNotice") val hasPreviousNotice: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("purposeOfNotice") @param:JsonProperty("purposeOfNotice") val purposeOfNotice: RecordPurposeOfNotice?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: MutableList<RecordRelatedProcess> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("preQualification") @param:JsonProperty("preQualification") val preQualification: RecordPreQualification?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("invitations") @param:JsonProperty("invitations") val invitations: List<RecordInvitation> = emptyList()
)
