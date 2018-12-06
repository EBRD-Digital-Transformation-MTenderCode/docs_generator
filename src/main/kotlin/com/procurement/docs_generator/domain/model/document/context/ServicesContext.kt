package com.procurement.docs_generator.domain.model.document.context

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.date.JsonDateDeserializer
import com.procurement.docs_generator.domain.date.JsonDateSerializer
import java.time.LocalDate

@JsonPropertyOrder("ac", "ev", "ms")
data class ServicesContext(
    @field:JsonProperty("ac") @param:JsonProperty("ac") val ac: AC,
    @field:JsonProperty("ev") @param:JsonProperty("ev") val ev: EV,
    @field:JsonProperty("ms") @param:JsonProperty("ms") val ms: MS
) {

    @JsonPropertyOrder("date", "contract", "tender", "buyer", "supplier", "award")
    data class AC(
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDate, // AC.date, format - (DD.MM.YYYY)

        @field:JsonProperty("contract") @param:JsonProperty("contract") val contract: Contract,
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender,
        @field:JsonProperty("buyer") @param:JsonProperty("buyer") val buyer: Buyer,
        @field:JsonProperty("supplier") @param:JsonProperty("supplier") val supplier: Supplier,
        @field:JsonProperty("award") @param:JsonProperty("award") val award: Award
    ) {

        @JsonPropertyOrder("id", "description", "value", "endDate", "agreedMetrics")
        data class Contract(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.contracts[0].id
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String, // AC.contracts[0].description,
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Double, // AC.contracts[0].value.amount

            @JsonSerialize(using = JsonDateSerializer::class)
            @JsonDeserialize(using = JsonDateDeserializer::class)
            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDate, // AC.contract.period.endDate (DD.MM.YYYY)

            @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: AgreedMetrics
        ) {

            @JsonPropertyOrder("ccGenerel_1_1Measure",
                               "ccGenerel_1_2Measure",
                               "ccGenerel_1_3Measure",
                               "ccBuyer_1_1Measure",
                               "ccBuyer_2_1Measure",
                               "ccBuyer_2_2Measure",
                               "ccTenderer_1_1Measure",
                               "ccTenderer_1_2Measure",
                               "ccTenderer_2_1Measure",
                               "ccTenderer_2_2Measure",
                               "ccTenderer_2_3Measure",
                               "ccTenderer_2_4Measure",
                               "ccTenderer_3_1Measure",
                               "ccTenderer_3_2Measure",
                               "ccTenderer_3_3Measure"
            )
            data class AgreedMetrics(
                @field:JsonProperty("ccGenerel_1_1Measure") @param:JsonProperty("ccGenerel_1_1Measure") val ccGenerel_1_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-general].observations[id==cc-general-1-1].measure
                @field:JsonProperty("ccGenerel_1_2Measure") @param:JsonProperty("ccGenerel_1_2Measure") val ccGenerel_1_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-general].observations[id==cc-general-1-2].measure
                @field:JsonProperty("ccGenerel_1_3Measure") @param:JsonProperty("ccGenerel_1_3Measure") val ccGenerel_1_3Measure: String, // AC.contracts[0].agreedMetrics[id==cc-general].observations[id==cc-general-1-3].measure

                @field:JsonProperty("ccBuyer_1_1Measure") @param:JsonProperty("ccBuyer_1_1Measure") val ccBuyer_1_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-buyer-1].observations[id==cc-buyer-1-1].measure
                @field:JsonProperty("ccBuyer_2_1Measure") @param:JsonProperty("ccBuyer_2_1Measure") val ccBuyer_2_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-buyer-1].observations[id==cc-buyer-2-1].measure
                @field:JsonProperty("ccBuyer_2_2Measure") @param:JsonProperty("ccBuyer_2_2Measure") val ccBuyer_2_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-buyer-1].observations[id==cc-buyer-2-2].measure

                @field:JsonProperty("ccTenderer_1_1Measure") @param:JsonProperty("ccTenderer_1_1Measure") val ccTenderer_1_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-1-1].measure
                /* TODO ccTenderer_1_6Measure */
                @field:JsonProperty("ccTenderer_1_2Measure") @param:JsonProperty("ccTenderer_1_2Measure") val ccTenderer_1_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-1-2].measure
                @field:JsonProperty("ccTenderer_2_1Measure") @param:JsonProperty("ccTenderer_2_1Measure") val ccTenderer_2_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-2-1].measure
                @field:JsonProperty("ccTenderer_2_2Measure") @param:JsonProperty("ccTenderer_2_2Measure") val ccTenderer_2_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-2-2].measure
                @field:JsonProperty("ccTenderer_2_3Measure") @param:JsonProperty("ccTenderer_2_3Measure") val ccTenderer_2_3Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-2-3].measure
                @field:JsonProperty("ccTenderer_2_4Measure") @param:JsonProperty("ccTenderer_2_4Measure") val ccTenderer_2_4Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-2-4].measure
                @field:JsonProperty("ccTenderer_3_1Measure") @param:JsonProperty("ccTenderer_3_1Measure") val ccTenderer_3_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-3-1].measure
                @field:JsonProperty("ccTenderer_3_2Measure") @param:JsonProperty("ccTenderer_3_2Measure") val ccTenderer_3_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-3-2].measure
                @field:JsonProperty("ccTenderer_3_3Measure") @param:JsonProperty("ccTenderer_3_3Measure") val ccTenderer_3_3Measure: String // AC.contracts[0].agreedMetrics[id==cc-tenderer-1].observations[id==cc-tenderer-3-3].measure
            )
        }

        @JsonPropertyOrder("procurementMethodDetails", "classification")
        data class Tender(
            @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String, // AC.tender.procurementMethodDetails
            @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification
        ) {

            @JsonPropertyOrder("id", "description")
            data class Classification(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.tender.classification.id
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.tender.classification.description
            )
        }

        @JsonPropertyOrder("address",
                           "identifier",
                           "additionalIdentifiers",
                           "contactPoint",
                           "persones",
                           "details"
        )
        data class Buyer(
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>,
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details
        ) {

            @JsonPropertyOrder("country", "region", "locality", "streetAddress", "postalCode")
            data class Address(
                @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties[role=="buyer"].address.addressDetails.country.description
                @field:JsonProperty("region") @param:JsonProperty("region") val region: String, // AC.parties[role=="buyer"].address.addressDetails.region.description
                @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties[role=="buyer"].address.addressDetails.locality.description
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties[role=="buyer"].address.streetAddress
                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String? // AC.parties[role=="buyer"].address.postalCode
            )

            @JsonPropertyOrder("id", "legalName")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.parties[role=="buyer"].identifier.id
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String // AC.parties[role=="buyer"].identifier.legalName
            )

            @JsonPropertyOrder("id")
            data class AdditionalIdentifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String // AC.parties.additionalIdentifiers[scheme=="MD-FISCAL"].id
            )

            @JsonPropertyOrder("telephone")
            data class ContactPoint(
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String // AC.parties.[role=="buyer"].contactPoint.telephone
            )

            @JsonPropertyOrder("title", "name", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String, // AC.parties[role=="buyer"].persones[*].title
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties[role=="buyer"].persones[*[.name
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("jobTitle")
                data class BusinessFunction(
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String // AC.parties[role=="buyer"].persones[*].businessFunctions[type=="authority"].jobTitle
                )
            }

            @JsonPropertyOrder("bankAccount", "legalForm", "permit")
            data class Details(
                @field:JsonProperty("bankAccount") @param:JsonProperty("bankAccount") val bankAccount: BankAccounts,
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm,
                @field:JsonProperty("permit") @param:JsonProperty("permit") val permit: Permit

            ) {

                @JsonPropertyOrder("accountIdentification", "identifier", "name", "address")
                data class BankAccounts(
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].accountIdentification.id
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].identifier.id
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].bankName
                    @field:JsonProperty("address") @param:JsonProperty("address") val address: Address
                ) {

                    @JsonPropertyOrder("country", "region", "locality", "streetAddress", "postalCode")
                    data class Address(
                        @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].address.addressDetails.country.description
                        @field:JsonProperty("region") @param:JsonProperty("region") val region: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].address.addressDetails.region.description
                        @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].address.addressDetails.locality.description
                        @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties.[role=="buyer"].details.bankAccounts[0].address.streetAddress
                        @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?// AC.parties.[role=="buyer"].details.bankAccounts[0].address.postalCode
                    )
                }

                @JsonPropertyOrder("description")
                data class LegalForm(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.parties[role=="buyer"].legalForm.description
                )

                @JsonPropertyOrder("id", "startDate")
                data class Permit(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?, // AC.parties.[role=="buyer"].details.permits[scheme="MD-SRLE"][0].id

                    @JsonSerialize(using = JsonDateSerializer::class)
                    @JsonDeserialize(using = JsonDateDeserializer::class)
                    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDate? // AC.parties.[role=="buyer"].details.permits[scheme="MD-SRLE"].permit.validityPeriod.startDate
                )
            }
        }

        @JsonPropertyOrder("address",
                           "identifier",
                           "additionalIdentifiers",
                           "contactPoint",
                           "persones",
                           "details")
        data class Supplier(
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>,
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details
        ) {

            @JsonPropertyOrder("country", "region", "locality", "streetAddress", "postalCode")
            data class Address(
                @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties[role=="supplier"].address.addressDetails.country.description
                @field:JsonProperty("region") @param:JsonProperty("region") val region: String, // AC.parties[role=="supplier"].address.addressDetails.region.description
                @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties[role=="supplier"].address.addressDetails.locality.description
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties[role=="supplier"].address.streetAddress
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String? // AC.parties[role=="supplier"].address.postalCode
            )

            @JsonPropertyOrder("id", "legalName")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String, // AC.parties[role=="supplier"].identifier.id
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String // AC.parties[role=="supplier"].identifier.legalName
            )

            @JsonPropertyOrder("id")
            data class AdditionalIdentifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String // AC.parties.[role=="supplier"].additionalidentifieres[scheme:MD-FISCAL].id

            )

            @JsonPropertyOrder("telephone")
            data class ContactPoint(
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String // AC.parties.[role=="supplier"].contactPoint.telephone
            )

            @JsonPropertyOrder("title", "name", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String, // AC.parties[role=="supplier"].persones[*].title
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties[role=="supplier"].persones[*].name
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("jobTitle", "documents")
                data class BusinessFunction(
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String, // AC.parties[role=="supplier"].persones.businessFunctions[type=="authority"].jobTitle
                    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>
                ) {

                    @JsonPropertyOrder("title")
                    data class Document(
                        @field:JsonProperty("title") @param:JsonProperty("title") val title: String // AC.parties[role=="supplier"].persones[*].businessFunctions[type=="authority"].documents[documentType=="regulatoryDocument"]
                    )
                }
            }

            @JsonPropertyOrder("bankAccount", "legalForm", "permit")
            data class Details(
                @field:JsonProperty("bankAccount") @param:JsonProperty("bankAccount") val bankAccount: BankAccounts,
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm,
                @field:JsonProperty("permit") @param:JsonProperty("permit") val permit: Permit
            ) {

                @JsonPropertyOrder("accountIdentification", "identifier", "name", "address")
                data class BankAccounts(
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].accountIdentification.id
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].identifier.id
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].bankName
                    @field:JsonProperty("address") @param:JsonProperty("address") val address: Address
                ) {

                    @JsonPropertyOrder("country", "region", "locality", "streetAddress", "postalCode")
                    data class Address(
                        @field:JsonProperty("country") @param:JsonProperty("country") val country: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].address.addressDetails.country.description
                        @field:JsonProperty("region") @param:JsonProperty("region") val region: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].address.addressDetails.region.description
                        @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].address.addressDetails.locality.description
                        @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String, // AC.parties.[role=="supplier"].details.bankAccounts[0].address.streetAddress
                        @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String? // AC.parties.[role=="supplier"].details.bankAccounts[0].address.postalCode
                    )
                }

                @JsonPropertyOrder("description")
                data class LegalForm(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String // AC.parties[role=="supplier"].legalForm.description
                )

                @JsonPropertyOrder("id", "startDate")
                data class Permit(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?, // AC.parties.[role=="supplier"].details.permits[scheme="MD-SRLE"][0].id

                    @JsonSerialize(using = JsonDateSerializer::class)
                    @JsonDeserialize(using = JsonDateDeserializer::class)
                    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDate? // AC.parties.[role=="supplier"].details.permits[scheme="MD-SRLE"].permit.validityPeriod.startDate
                )
            }
        }

        @JsonPropertyOrder("date", "relatedLot", "items")
        data class Award(
            @JsonSerialize(using = JsonDateSerializer::class)
            @JsonDeserialize(using = JsonDateDeserializer::class)
            @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDate, //AC.awards[relatedLots[0]==AC.tender.lots[0].id].date (DD.MM.YYYY)

            @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: RelatedLot,
            @field:JsonProperty("items") @param:JsonProperty("items") val items: List<Item>
        ) {

            @JsonPropertyOrder("id")
            data class RelatedLot(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String //AC.awards.relatedLots[0]
            )

            @JsonPropertyOrder("classification", "description", "unit", "planning", "quantity", "agreedMetrics")
            data class Item(
                @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,//AC.award.items[*].description
                @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit,
                @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: Planning,
                @field:JsonProperty("quantity") @param:JsonProperty("quantity") val quantity: Double, //AC.award.items[*].quantity
                @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: AgreedMetrics
            ) {

                @JsonPropertyOrder("id", "description")
                data class Classification(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,// AC.award.items[*].classification.id
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String// AC.award.items[*].classification.description
                )

                @JsonPropertyOrder("name", "value")
                data class Unit(
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String,//AC.award.items[*].unit.name
                    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
                ) {

                    @JsonPropertyOrder("amountNet", "amount")
                    data class Value(
                        @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: Double,//AC.award.items[*].unit.value.amountNet
                        @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Double//AC.award.items[*].unit.value.amount
                    )
                }

                @JsonPropertyOrder("budgetAllocation")
                data class Planning(
                    @field:JsonProperty("budgetAllocation") @param:JsonProperty("budgetAllocation") val budgetAllocation: BudgetAllocation

                ) {

                    @JsonPropertyOrder("period", "budgetBreakdownID")
                    data class BudgetAllocation(
                        @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
                        @field:JsonProperty("budgetBreakdownID") @param:JsonProperty("budgetBreakdownID") val budgetBreakdownID: String// AC.planning.budget.budgetAllocation[relatedItem==item.id].budgetBreakdownID
                    ) {

                        @JsonPropertyOrder("startDate", "endDate")
                        data class Period(
                            @JsonSerialize(using = JsonDateSerializer::class)
                            @JsonDeserialize(using = JsonDateDeserializer::class)
                            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDate, // AC.planning.budget.budgetAllocation[relatedItem==item.id].period.startDate, format - (DD.MM.YYYY)

                            @JsonSerialize(using = JsonDateSerializer::class)
                            @JsonDeserialize(using = JsonDateDeserializer::class)
                            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDate // AC.planning.budget.budgetAllocation[relatedItem==item.id].period.endDate, format - (DD.MM.YYYY)
                        )
                    }
                }

                @JsonPropertyOrder("ccSubject_1Measure",
                                   "ccSubject_2Measure",
                                   "ccSubject_3Measure",
                                   "ccSubject_4Measure",
                                   "ccSubject_5Measure")
                data class AgreedMetrics(
                    @field:JsonProperty("ccSubject_1Measure") @param:JsonProperty("ccSubject_1Measure") val ccSubject_1Measure: String, // AC.contracts[0].agreedMetrics[id==cc-subject-["item.id"]-*].observations[id==cc-subject-1].measure
                    @field:JsonProperty("ccSubject_2Measure") @param:JsonProperty("ccSubject_2Measure") val ccSubject_2Measure: String, // AC.contracts[0].agreedMetrics[id==cc-subject-["item.id"]-*].observations[id==cc-subject-2].measure
                    @field:JsonProperty("ccSubject_3Measure") @param:JsonProperty("ccSubject_3Measure") val ccSubject_3Measure: String, // AC.contracts[0].agreedMetrics[id==cc-subject-["item.id"]-*].observations[id==cc-subject-3].measure
                    @field:JsonProperty("ccSubject_4Measure") @param:JsonProperty("ccSubject_4Measure") val ccSubject_4Measure: String, // AC.contracts[0].agreedMetrics[id==cc-subject-["item.id"]-*].observations[id==cc-subject-4].measure
                    @field:JsonProperty("ccSubject_5Measure") @param:JsonProperty("ccSubject_5Measure") val ccSubject_5Measure: String // AC.contracts[0].agreedMetrics[id==cc-subject-["item.id"]-*].observations[id==cc-subject-5].measure
                )
            }
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

    @JsonPropertyOrder("tender")
    data class MS(
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender
    ) {

        @JsonPropertyOrder("id", "title")
        data class Tender(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,//MS.tender.id
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String //MS.tender.title
        )
    }
}