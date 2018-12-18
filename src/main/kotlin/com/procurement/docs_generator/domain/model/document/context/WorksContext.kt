package com.procurement.docs_generator.domain.model.document.context

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.date.JsonDateDeserializer
import com.procurement.docs_generator.domain.date.JsonDateSerializer
import java.time.LocalDate

@JsonPropertyOrder("ac", "ev")
data class WorksContext(
    @field:JsonProperty("ac") @param:JsonProperty("ac") val ac: AC,
    @field:JsonProperty("ev") @param:JsonProperty("ev") val ev: EV
) {

    @JsonPropertyOrder("date", "contract", "buyer", "supplier", "tender")
    data class AC(
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDate, // AC.date, format - (DD.MM.YYYY)
        @field:JsonProperty("contract") @param:JsonProperty("contract") val contract: Contract,
        @field:JsonProperty("buyer") @param:JsonProperty("buyer") val buyer: Buyer,
        @field:JsonProperty("supplier") @param:JsonProperty("supplier") val supplier: Supplier,
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender

    ) {
        @JsonPropertyOrder("id", "description", "monthNumber", "amount", "amountNet", "agreedMetrics")
        data class Contract(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.contracts[0].id
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String, // AC.contracts[0].description

            @field:JsonProperty("monthNumber") @param:JsonProperty("monthNumber") val monthNumber: Int, // AC.contract[0].period.endDate - AC.contract[0].period.startDate  (in month)

            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: String, // AC.contract[0].value.amount
            @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: String, // AC.contract[0].value.amountNet
            @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: AgreedMetrics
        ) {
            @JsonPropertyOrder(
                "ccTenderer_1_1Measure",
                "ccTenderer_1_5Measure",
                "ccTenderer_2_3Measure",
                "ccTenderer_2_3Measure",
                "ccTenderer_3_2Measure",
                "ccTenderer_3_3Measure",
                "ccBuyer_1_1Measure"
            )
            data class AgreedMetrics(
                @field:JsonProperty("ccTenderer_1_1Measure") @param:JsonProperty("ccTenderer_1_1Measure") val ccTenderer_1_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-1-1].measure
                @field:JsonProperty("ccTenderer_1_5Measure") @param:JsonProperty("ccTenderer_1_5Measure") val ccTenderer_1_5Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-1-5].measure
                @field:JsonProperty("ccTenderer_2_3Measure") @param:JsonProperty("ccTenderer_2_3Measure") val ccTenderer_2_3Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-2].observations[id==cc-tenderer-2-3].measure
                @field:JsonProperty("ccTenderer_2_2Measure") @param:JsonProperty("ccTenderer_2_2Measure") val ccTenderer_2_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-2].observations[id==cc-tenderer-2-2].measure
                @field:JsonProperty("ccTenderer_3_2Measure") @param:JsonProperty("ccTenderer_3_2Measure") val ccTenderer_3_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-3].observations[id==cc-tenderer-3-2].measure
                @field:JsonProperty("ccTenderer_3_3Measure") @param:JsonProperty("ccTenderer_3_3Measure") val ccTenderer_3_3Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-3].observations[id==cc-tenderer-3-3].measure
                @field:JsonProperty("ccBuyer_1_1Measure") @param:JsonProperty("ccBuyer_1_1Measure") val ccBuyer_1_1Measure: String // AC.contracts[0].agreedMetrics[id==cc-buyer-1].observations[id==cc-buyer-1-1].measure
            )
        }

        @JsonPropertyOrder(
            "address",
            "details",
            "identifier",
            "contactPoint",
            "persones",
            "additionalIdentifier"
        )
        data class Buyer(
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>,
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>

        ) {
            @JsonPropertyOrder("legalForm", "permits", "bankAccount")
            data class Details(
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm,
                @field:JsonProperty("permit") @param:JsonProperty("permit") val permit: Permit?,
                @field:JsonProperty("bankAccount") @param:JsonProperty("bankAccount") val bankAccount: BankAccounts
            ) {
                @JsonPropertyOrder("description")
                data class LegalForm(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.parties[role=="buyer"].legalForm.description
                )

                @JsonPropertyOrder("id", "startDate")
                data class Permit(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.parties.[role=="buyer"].details.permits[scheme="SRL"][0].id

                    @JsonSerialize(using = JsonDateSerializer::class)
                    @JsonDeserialize(using = JsonDateDeserializer::class)
                    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDate // AC.parties.[role=="buyer"].details.permits[scheme="SRL"].permit.validityPeriod.startDate
                )

                @JsonPropertyOrder("accountIdentification", "identifier", "name")
                data class BankAccounts(
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].accountIdentification.id
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].identifier.id
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String // AC.parties.[role=="buyer"].details.bankAccounts[0].bankName
                )
            }

            @JsonPropertyOrder("locality", "postalCode", "streetAddress", "country", "region")
            data class Address(
                @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties[role=="buyer"].address.addressDetails.locality.description
                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?, // AC.parties[role=="buyer"].address.postalCode
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties[role=="buyer"].address.streetAddress
                @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties[role=="buyer"].address.addressDetails.country.description
                @field:JsonProperty("region") @param:JsonProperty("region") val region: String // AC.parties[role=="buyer"].address.addressDetails.region.description
            )

            @JsonPropertyOrder("id", "legalName")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.parties[role=="buyer"].identifier.id
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String // AC.parties[role=="buyer"].identifier.legalName
            )

            @JsonPropertyOrder("telephone", "fax")
            data class ContactPoint(
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String, // AC.parties.[role=="buyer"].contactPoint.telephone
                @field:JsonProperty("fax") @param:JsonProperty("fax") val fax: String? // AC.parties.[role=="buyer"].contactPoint.fax
            )

            @JsonPropertyOrder("title", "name", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String, // AC.parties[role=="buyer"].persones[*].title
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties[role=="buyer"].persones[*].name
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("jobTitle")
                data class BusinessFunction(
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String // AC.parties[role=="buyer"].persones[*].businessFunctions[type=="authority"].jobTitle
                )
            }

            @JsonPropertyOrder("id")
            data class AdditionalIdentifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String?// AC.parties.additionalIdentifiers[scheme=="MD-FISCAL"].id
            )
        }

        @JsonPropertyOrder(
            "address",
            "identifier",
            "contactPoint",
            "persones",
            "details",
            "additionalIdentifiers")
        data class Supplier(
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>,
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details,
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>
        ) {
            @JsonPropertyOrder("locality", "streetAddress", "postalCode", "country", "region")
            data class Address(
                @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties[role=="supplier"].address.addressDetails.locality.description
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties[role=="supplier"].address.streetAddress
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?, // AC.parties[role=="supplier"].address.postalCode
                @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties[role=="supplier"].address.addressDetails.country.description
                @field:JsonProperty("region") @param:JsonProperty("region") val region: String // AC.parties[role=="supplier"].address.addressDetails.region.description
            )

            @JsonPropertyOrder("id", "legalName")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.parties[role=="supplier"].identifier.id
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String // AC.parties[role=="supplier"].identifier.legalName
            )

            @JsonPropertyOrder("telephone", "fax")
            data class ContactPoint(
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String, // AC.parties.[role=="supplier"].contactPoint.telephone
                @field:JsonProperty("fax") @param:JsonProperty("fax") val fax: String? // AC.parties.[role=="supplier"].contactPoint.fax
            )

            @JsonPropertyOrder("title", "name", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String, // AC.parties[role=="supplier"].persones[*].title
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties[role=="supplier"].persones[*].name
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("jobTitle")
                data class BusinessFunction(
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String // AC.parties[role=="supplier"].persones.businessFunctions[type=="authority"].jobTitle
                )
            }

            @JsonPropertyOrder("legalForm", "permit", "bankAccount")
            data class Details(
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm,
                @field:JsonProperty("permit") @param:JsonProperty("permit") val permit: Permit?,
                @field:JsonProperty("bankAccount") @param:JsonProperty("bankAccount") val bankAccount: BankAccounts
            ) {
                @JsonPropertyOrder("description")
                data class LegalForm(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.parties[role=="supplier"].legalForm.description
                )

                @JsonPropertyOrder("idSRL", "startDateSRL", "idSRLE", "startDateSRLE", "yearsNumber", "issuedBy", "issuedThought")
                data class Permit(
                    @field:JsonProperty("idSRL") @param:JsonProperty("idSRL") val idSRL: String, // AC.parties.[role=="supplier"].details.permits[scheme="SRL"][0].id

                    @JsonSerialize(using = JsonDateSerializer::class)
                    @JsonDeserialize(using = JsonDateDeserializer::class)
                    @field:JsonProperty("startDateSRL") @param:JsonProperty("startDateSRL") val startDateSRL: LocalDate, // AC.parties.[role=="supplier"].details.permits[scheme="SRL"].permitDetails.validityPeriod.startDate

                    @field:JsonProperty("idSRLE") @param:JsonProperty("idSRLE") val idSRLE: String, // AC.parties.[role=="supplier"].details.permits[scheme="SRLE"][0].id

                    @JsonSerialize(using = JsonDateSerializer::class)
                    @JsonDeserialize(using = JsonDateDeserializer::class)
                    @field:JsonProperty("startDateSRLE") @param:JsonProperty("startDateSRLE") val startDateSRLE: LocalDate, // AC.parties.[role=="supplier"].details.permits[scheme="SRLE"].permitDetails.validityPeriod.startDate
                    @field:JsonProperty("yearsNumber") @param:JsonProperty("yearsNumber") val yearsNumber: Int?, // AC.parties.[role=="supplier"].details.permits[scheme="SRLE"].permitDetails.validityPeriod.endDate - AC.parties.[role=="supplier"].details.permits[scheme="SRLE"].permitDetails.validityPeriod.startDate (in years)

                    @field:JsonProperty("issuedBy") @param:JsonProperty("issuedBy") val issuedBy: IssuedBy,//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedBy
                    @field:JsonProperty("issuedThought") @param:JsonProperty("issuedThought") val issuedThought: IssuedThought//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedThought
                ) {
                    data class IssuedBy(
                        @field:JsonProperty("name") @param:JsonProperty("name") val name: String,//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedBy.name
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedBy.id
                    )

                    data class IssuedThought(
                        @field:JsonProperty("name") @param:JsonProperty("name") val name: String,//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedThought.name
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String//AC.parties.[supplier].details.permits[scheme:SRLE].permitDetails.issuedThought.id
                    )
                }

                @JsonPropertyOrder("accountIdentification", "identifier", "name")
                data class BankAccounts(
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].accountIdentification.id
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].identifier.id
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String // AC.parties.[role=="supplier"].details.bankAccounts[0].bankName
                )
            }

            @JsonPropertyOrder("id")
            data class AdditionalIdentifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String? // AC.parties.[role=="supplier"].additionalidentifieres[scheme:MD-FISCAL].id
            )
        }

        @JsonPropertyOrder("classification")
        data class Tender(
            @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification
        ) {

            @JsonPropertyOrder("id", "description")
            data class Classification(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.tender.classification.id
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.tender.classification.description
            )
        }

    }

    @JsonPropertyOrder("publishDate", "tender")
    data class EV(
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @field:JsonProperty("publishDate") @param:JsonProperty("publishDate") val publishDate: LocalDate, // EV.publishDate, format - (DD.MM.YYYY)

        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender
    ) {

        @JsonPropertyOrder("id")
        data class Tender(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String // EV.tender.id
        )
    }
}