package com.procurement.docs_generator.domain.model.release.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.docs_generator.domain.model.release.entity.RecordBusinessFunction
import com.procurement.docs_generator.domain.model.release.entity.RecordIdentifier

data class RecordPerson(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: PersonId?,

    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @field:JsonProperty("name") @param:JsonProperty("name") val name: String,

    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RecordIdentifier,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<RecordBusinessFunction> = emptyList()
)
