package com.procurement.docs_generator.domain.model.release

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.docs_generator.domain.model.date.JsonDateTimeDeserializer
import com.procurement.docs_generator.domain.model.date.JsonDateTimeSerializer
import java.math.BigDecimal
import java.time.LocalDateTime

@JsonPropertyOrder("releases")
class ACReleasesPackage(
    @JsonProperty("releases") val releases: List<Release>
) {
    @JsonPropertyOrder("date", "tender", "planning", "contracts", "awards", "parties", "relatedProcesses")
    data class Release(
        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender,
        @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: Planning,
        @field:JsonProperty("contracts") @param:JsonProperty("contracts") val contracts: List<Contract>,
        @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<Award>,
        @field:JsonProperty("parties") @param:JsonProperty("parties") val parties: List<Party>,
        @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: List<RelatedProcess>
    ) {

        @JsonPropertyOrder("classification", "procurementMethodDetails", "mainProcurementCategory", "lots")
        data class Tender(
            @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
            @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,
            @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
            @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<Lot>
        ) {

            @JsonPropertyOrder("id")
            data class Lot(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String
            )

            @JsonPropertyOrder("description", "id")
            data class Classification(
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String
            )
        }

        @JsonPropertyOrder("budget")
        data class Planning(
            @field:JsonProperty("budget") @param:JsonProperty("budget") val budget: Budget
        ) {

            @JsonPropertyOrder("budgetAllocation")
            data class Budget(
                @field:JsonProperty("budgetAllocation") @param:JsonProperty("budgetAllocation") val budgetAllocation: List<BudgetAllocation>
            ) {

                @JsonPropertyOrder("budgetBreakdownID", "period", "relatedItem")
                data class BudgetAllocation(
                    @field:JsonProperty("budgetBreakdownID") @param:JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,
                    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
                    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String
                ) {

                    @JsonPropertyOrder("startDate", "endDate")
                    data class Period(
                        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                        @JsonSerialize(using = JsonDateTimeSerializer::class)
                        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

                        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                        @JsonSerialize(using = JsonDateTimeSerializer::class)
                        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
                    )
                }
            }
        }

        @JsonPropertyOrder("id", "description", "period", "agreedMetrics", "value")
        data class Contract(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
            @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
            @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<AgreedMetric>,
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
        ) {

            @JsonPropertyOrder("endDate")
            data class Period(
                @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                @JsonSerialize(using = JsonDateTimeSerializer::class)
                @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
            )

            @JsonPropertyOrder("id", "observations")
            data class AgreedMetric(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("observations") @param:JsonProperty("observations") val observations: List<Observation>
            ) {

                @JsonPropertyOrder("id", "measure")
                data class Observation(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("measure") @param:JsonProperty("measure") val measure: Int
                )
            }

            @JsonPropertyOrder("amount")
            data class Value(
                @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal
            )
        }

        @JsonPropertyOrder("id", "date", "relatedLots", "items")
        data class Award(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,

            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>,
            @field:JsonProperty("items") @param:JsonProperty("items") val items: List<Item>
        ) {

            @JsonPropertyOrder("id", "classification", "quantity", "unit", "description")
            data class Item(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
                @field:JsonProperty("quantity") @param:JsonProperty("quantity") val quantity: BigDecimal,
                @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
            ) {

                @JsonPropertyOrder("description", "id")
                data class Classification(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                )

                @JsonPropertyOrder("value", "name")
                data class Unit(
                    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                ) {

                    @JsonPropertyOrder("amount", "amountNet")
                    data class Value(
                        @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                        @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: BigDecimal
                    )
                }
            }
        }

        @JsonPropertyOrder("identifier",
                           "address",
                           "contactPoint",
                           "additionalIdentifiers",
                           "roles",
                           "persones",
                           "details")
        data class Party(
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @field:JsonProperty("roles") @param:JsonProperty("roles") val roles: List<String>,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>?,

            //TODO for Buyer and Supplier is required
//            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details
        ) {

            @JsonPropertyOrder("id", "legalName")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String
            )

            @JsonPropertyOrder("streetAddress", "postalCode", "addressDetails")
            data class Address(
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,
                @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
            ) {

                @JsonPropertyOrder("country", "region", "locality")
                data class AddressDetails(
                    @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                    @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                    @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                ) {

                    @JsonPropertyOrder("description")
                    data class Country(
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                    )

                    @JsonPropertyOrder("description")
                    data class Region(
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                    )

                    @JsonPropertyOrder("description")
                    data class Locality(
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                    )
                }
            }

            @JsonPropertyOrder("telephone")
            data class ContactPoint(
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String
            )

            @JsonPropertyOrder("scheme", "id")
            data class AdditionalIdentifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String
            )

            @JsonPropertyOrder("title", "name", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("type", "jobTitle", "documents")
                data class BusinessFunction(

                    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String,
                    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>
                ) {

                    @JsonPropertyOrder("documentType", "title")
                    data class Document(
                        @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
                        @field:JsonProperty("title") @param:JsonProperty("title") val title: String
                    )
                }
            }

            @JsonPropertyOrder("permits", "bankAccounts", "legalForm")
            data class Details(
                @JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("permits") @param:JsonProperty("permits") val permits: List<Permit>?,
                @field:JsonProperty("bankAccounts") @param:JsonProperty("bankAccounts") val bankAccounts: List<BankAccount>,
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm
            ) {

                @JsonPropertyOrder("scheme", "id", "permitDetails")
                data class Permit(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("permitDetails") @param:JsonProperty("permitDetails") val permitDetails: PermitDetails
                ) {

                    @JsonPropertyOrder("validityPeriod")
                    data class PermitDetails(
                        @field:JsonProperty("validityPeriod") @param:JsonProperty("validityPeriod") val validityPeriod: ValidityPeriod
                    ) {

                        @JsonPropertyOrder("startDate")
                        data class ValidityPeriod(
                            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
                            @JsonSerialize(using = JsonDateTimeSerializer::class)
                            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime
                        )
                    }
                }

                @JsonPropertyOrder("bankName", "address", "identifier", "accountIdentification")
                data class BankAccount(
                    @field:JsonProperty("bankName") @param:JsonProperty("bankName") val bankName: String,
                    @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: AccountIdentification
                ) {

                    @JsonPropertyOrder("streetAddress", "postalCode", "addressDetails")
                    data class Address(
                        @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,

                        @field:JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,
                        @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
                    ) {

                        @JsonPropertyOrder("country", "region", "locality")
                        data class AddressDetails(
                            @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                            @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                            @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                        ) {

                            @JsonPropertyOrder("description")
                            data class Country(
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                            )

                            @JsonPropertyOrder("description")
                            data class Region(
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                            )

                            @JsonPropertyOrder("description")
                            data class Locality(
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                            )
                        }
                    }

                    @JsonPropertyOrder("id")
                    data class Identifier(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                    )

                    @JsonPropertyOrder("id")
                    data class AccountIdentification(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                    )
                }

                @JsonPropertyOrder("description")
                data class LegalForm(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String
                )
            }
        }

        @JsonPropertyOrder("identifier", "relationship")
        data class RelatedProcess(
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String,
            @field:JsonProperty("relationship") @param:JsonProperty("relationship") val relationship: List<String>
        )
    }
}