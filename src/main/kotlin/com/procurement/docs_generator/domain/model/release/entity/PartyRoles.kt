package com.procurement.docs_generator.domain.model.release.entity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.*

enum class PartyRole constructor(private val value: String) {
    BUYER("buyer"),
    CANDIDATE("candidate"),
    ENQUIRER("enquirer"),
    FUNDER("funder"),
    INVITED_CANDIDATE("invitedCandidate"),
    INVITED_TENDERER("invitedTenderer"),
    PAYEE("payee"),
    PAYER("payer"),
    PROCURING_ENTITY("procuringEntity"),
    REVIEW_BODY("reviewBody"),
    SUPPLIER("supplier"),
    TENDERER("tenderer");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, PartyRole>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): PartyRole {
            return CONSTANTS[value] ?: throw IllegalArgumentException(value)
        }
    }
}