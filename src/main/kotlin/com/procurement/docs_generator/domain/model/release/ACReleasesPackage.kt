package com.procurement.docs_generator.domain.model.release

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.math.BigDecimal

@JsonPropertyOrder("releases")
class ACReleasesPackage(
    @JsonProperty("releases") val releases: List<Release>
) {
    @JsonPropertyOrder("ocid",
                       "id",
                       "date",
                       "tag",
                       "initiationType",
                       "tender",
                       "planning",
                       "contracts",
                       "awards",
                       "parties",
                       "relatedProcesses")
    data class Release(
        @field:JsonProperty("ocid") @param:JsonProperty("ocid") val ocid: String,
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("date") @param:JsonProperty("date") val date: String,
        @field:JsonProperty("tag") @param:JsonProperty("tag") val tag: List<String>,
        @field:JsonProperty("initiationType") @param:JsonProperty("initiationType") val initiationType: String,
        @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender,
        @field:JsonProperty("planning") @param:JsonProperty("planning") val planning: Planning,
        @field:JsonProperty("contracts") @param:JsonProperty("contracts") val contracts: List<Contract>,
        @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<Award>,
        @field:JsonProperty("parties") @param:JsonProperty("parties") val parties: List<Party>,
        @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: List<RelatedProcess>
    ) {

        @JsonPropertyOrder("id",
                           "classification",
                           "procurementMethod",
                           "procurementMethodDetails",
                           "mainProcurementCategory",
                           "lots")
        data class Tender(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,
            @field:JsonProperty("procurementMethod") @param:JsonProperty("procurementMethod") val procurementMethod: String,
            @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,
            @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
            @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<Lot>
        ) {

            @JsonPropertyOrder("id", "title", "description", "placeOfPerformance")
            data class Lot(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("placeOfPerformance") @param:JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance
            ) {

                @JsonPropertyOrder("address", "description")
                data class PlaceOfPerformance(
                    @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,

                    @field:JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
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
                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Region(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                            )

                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Locality(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                            )

                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Country(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                            )
                        }
                    }
                }
            }

            @JsonPropertyOrder("scheme", "description", "id")
            data class Classification(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String
            )
        }

        @JsonPropertyOrder("implementation", "budget")
        data class Planning(
            @field:JsonProperty("implementation") @param:JsonProperty("implementation") val implementation: Implementation,
            @field:JsonProperty("budget") @param:JsonProperty("budget") val budget: Budget
        ) {

            @JsonPropertyOrder("transactions")
            data class Implementation(
                @field:JsonProperty("transactions") @param:JsonProperty("transactions") val transactions: List<Transaction>
            ) {

                @JsonPropertyOrder("id", "type", "value", "executionPeriod", "relatedContractMilestone")
                data class Transaction(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
                    @field:JsonProperty("executionPeriod") @param:JsonProperty("executionPeriod") val executionPeriod: ExecutionPeriod,

                    @field:JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("relatedContractMilestone") @param:JsonProperty("relatedContractMilestone") val relatedContractMilestone: String?
                ) {

                    @JsonPropertyOrder("durationInDays")
                    data class ExecutionPeriod(
                        @field:JsonProperty("durationInDays") @param:JsonProperty("durationInDays") val durationInDays: Int
                    )

                    @JsonPropertyOrder("amount", "currency")
                    data class Value(
                        @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                        @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
                    )
                }
            }

            @JsonPropertyOrder("description", "budgetAllocation", "budgetSource")
            data class Budget(
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("budgetAllocation") @param:JsonProperty("budgetAllocation") val budgetAllocation: List<BudgetAllocation>,
                @field:JsonProperty("budgetSource") @param:JsonProperty("budgetSource") val budgetSource: List<BudgetSource>
            ) {

                @JsonPropertyOrder("budgetBreakdownID", "period", "amount", "relatedItem")
                data class BudgetAllocation(
                    @field:JsonProperty("budgetBreakdownID") @param:JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,
                    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
                    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String
                ) {

                    @JsonPropertyOrder("startDate", "endDate")
                    data class Period(
                        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String,
                        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: String
                    )
                }

                @JsonPropertyOrder("budgetBreakdownID", "amount", "currency")
                data class BudgetSource(
                    @field:JsonProperty("budgetBreakdownID") @param:JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,
                    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                    @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
                )
            }
        }

        @JsonPropertyOrder("id",
                           "date",
                           "awardId",
                           "status",
                           "statusDetails",
                           "title",
                           "description",
                           "period",
                           "documents",
                           "milestones",
                           "confirmationRequests",
                           "agreedMetrics",
                           "value")
        data class Contract(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("date") @param:JsonProperty("date") val date: String,
            @field:JsonProperty("awardId") @param:JsonProperty("awardId") val awardId: String,
            @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
            @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
            @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?,
            @field:JsonProperty("milestones") @param:JsonProperty("milestones") val milestones: List<Milestone>,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("confirmationRequests") @param:JsonProperty("confirmationRequests") val confirmationRequests: List<ConfirmationRequest>?,
            @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<AgreedMetric>,
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
        ) {

            @JsonPropertyOrder("startDate", "endDate")
            data class Period(
                @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String,
                @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: String
            )

            @JsonPropertyOrder("id", "title", "description", "observations")
            data class AgreedMetric(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("observations") @param:JsonProperty("observations") val observations: List<Observation>
            ) {

                @JsonPropertyOrder("id", "notes", "measure", "unit")
                data class Observation(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("notes") @param:JsonProperty("notes") val notes: String,
                    @field:JsonProperty("measure") @param:JsonProperty("measure") val measure: Int,

                    @field:JsonInclude(JsonInclude.Include.NON_NULL)
                    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit?
                ) {

                    @JsonPropertyOrder("name", "id", "scheme")
                    data class Unit(
                        @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String
                    )
                }
            }

            @JsonPropertyOrder("id",
                               "relatedItems",
                               "status",
                               "additionalInformation",
                               "dueDate",
                               "title",
                               "type",
                               "description",
                               "relatedParties")
            data class Milestone(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

                @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("relatedItems") @param:JsonProperty("relatedItems") val relatedItems: List<String>?,
                @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
                @field:JsonProperty("additionalInformation") @param:JsonProperty("additionalInformation") val additionalInformation: String,
                @field:JsonProperty("dueDate") @param:JsonProperty("dueDate") val dueDate: String,
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("relatedParties") @param:JsonProperty("relatedParties") val relatedParties: List<RelatedParty>
            ) {

                @JsonPropertyOrder("id", "name")
                data class RelatedParty(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                )
            }

            @JsonPropertyOrder("id",
                               "type",
                               "title",
                               "description",
                               "relatesTo",
                               "relatedItem",
                               "source",
                               "requestGroups")
            data class ConfirmationRequest(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String,
                @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String,
                @field:JsonProperty("source") @param:JsonProperty("source") val source: String,
                @field:JsonProperty("requestGroups") @param:JsonProperty("requestGroups") val requestGroups: List<RequestGroup>
            ) {

                @JsonPropertyOrder("id", "requests")
                data class RequestGroup(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("requests") @param:JsonProperty("requests") val requests: List<Request>
                ) {

                    @JsonPropertyOrder("id", "title", "description", "relatedPerson")
                    data class Request(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("relatedPerson") @param:JsonProperty("relatedPerson") val relatedPerson: RelatedPerson
                    ) {

                        @JsonPropertyOrder("id", "name")
                        data class RelatedPerson(
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                        )
                    }
                }
            }

            @JsonPropertyOrder("amount", "currency", "amountNet", "valueAddedTaxIncluded")
            data class Value(
                @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String,
                @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: BigDecimal,
                @field:JsonProperty("valueAddedTaxIncluded") @param:JsonProperty("valueAddedTaxIncluded") val valueAddedTaxIncluded: Boolean
            )

            @JsonPropertyOrder("documentType", "datePublished", "url", "id", "title", "description", "relatedLots")
            data class Document(
                @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
                @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: String,
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>
            )
        }

        @JsonPropertyOrder("id",
                           "date",
                           "description",
                           "relatedLots",
                           "relatedBid",
                           "suppliers",
                           "documents",
                           "value",
                           "items")
        data class Award(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("date") @param:JsonProperty("date") val date: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>,
            @field:JsonProperty("relatedBid") @param:JsonProperty("relatedBid") val relatedBid: String,
            @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<Supplier>,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?,
            @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
            @field:JsonProperty("items") @param:JsonProperty("items") val items: List<Item>
        ) {

            @JsonPropertyOrder("id", "name")
            data class Supplier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String
            )

            @JsonPropertyOrder("documentType", "datePublished", "url", "id", "title", "description", "relatedLots")
            data class Document(
                @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
                @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: String,
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

                @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>?
            )

            @JsonPropertyOrder("amount", "currency", "amountNet", "valueAddedTaxIncluded")
            data class Value(
                @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String,
                @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: BigDecimal,
                @field:JsonProperty("valueAddedTaxIncluded") @param:JsonProperty("valueAddedTaxIncluded") val valueAddedTaxIncluded: Boolean
            )

            @JsonPropertyOrder("id",
                               "classification",
                               "additionalClassifications",
                               "quantity",
                               "unit",
                               "description",
                               "relatedLot",
                               "deliveryAddress")
            data class Item(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: Classification,

                @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("additionalClassifications") @param:JsonProperty("additionalClassifications") val additionalClassifications: List<AdditionalClassification>?,
                @field:JsonProperty("quantity") @param:JsonProperty("quantity") val quantity: BigDecimal,
                @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,
                @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: String,
                @field:JsonProperty("deliveryAddress") @param:JsonProperty("deliveryAddress") val deliveryAddress: DeliveryAddress
            ) {

                @JsonPropertyOrder("scheme", "description", "id")
                data class Classification(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                )

                @JsonPropertyOrder("scheme", "description", "id")
                data class AdditionalClassification(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                )

                @JsonPropertyOrder("value", "id", "name")
                data class Unit(
                    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                ) {

                    @JsonPropertyOrder("amount", "amountNet", "valueAddedTaxIncluded", "currency")
                    data class Value(
                        @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                        @field:JsonProperty("amountNet") @param:JsonProperty("amountNet") val amountNet: BigDecimal,
                        @field:JsonProperty("valueAddedTaxIncluded") @param:JsonProperty("valueAddedTaxIncluded") val valueAddedTaxIncluded: Boolean,
                        @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
                    )
                }

                @JsonPropertyOrder("streetAddress", "postalCode", "addressDetails")
                data class DeliveryAddress(
                    @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,
                    @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String,
                    @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
                ) {

                    @JsonPropertyOrder("country", "region", "locality")
                    data class AddressDetails(
                        @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                        @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                        @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                    ) {

                        @JsonPropertyOrder("scheme", "id", "description", "uri")
                        data class Country(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                        )

                        @JsonPropertyOrder("scheme", "id", "description", "uri")
                        data class Region(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                        )

                        @JsonPropertyOrder("scheme", "id", "description", "uri")
                        data class Locality(
                            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                            @field:JsonInclude(JsonInclude.Include.NON_NULL)
                            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                        )
                    }
                }
            }
        }

        @JsonPropertyOrder("id",
                           "name",
                           "identifier",
                           "address",
                           "contactPoint",
                           "additionalIdentifiers",
                           "roles",
                           "persones",
                           "details")
        data class Party(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,
            @field:JsonProperty("roles") @param:JsonProperty("roles") val roles: List<String>,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("persones") @param:JsonProperty("persones") val persones: List<Person>?,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details?
        ) {

            @JsonPropertyOrder("id", "scheme", "legalName", "uri")
            data class Identifier(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,
                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
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

                    @JsonPropertyOrder("scheme", "id", "description", "uri")
                    data class Country(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    @JsonPropertyOrder("scheme", "id", "description", "uri")
                    data class Region(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    @JsonPropertyOrder("scheme", "id", "description", "uri")
                    data class Locality(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                        @field:JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                    )
                }
            }

            @JsonPropertyOrder("name", "email", "telephone", "faxNumber", "url")
            data class ContactPoint(
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("email") @param:JsonProperty("email") val email: String,
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("faxNumber") @param:JsonProperty("faxNumber") val faxNumber: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String?
            )

            @JsonPropertyOrder("scheme", "id", "legalName", "uri")
            data class AdditionalIdentifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
            )

            @JsonPropertyOrder("title", "name", "identifier", "businessFunctions")
            data class Person(
                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
                @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {

                @JsonPropertyOrder("scheme", "id", "uri")
                data class Identifier(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                )

                @JsonPropertyOrder("id", "type", "jobTitle", "period", "documents")
                data class BusinessFunction(
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
                    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String,
                    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period,
                    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>
                ) {

                    @JsonPropertyOrder("startDate")
                    data class Period(
                        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String
                    )

                    @JsonPropertyOrder("id", "datePublished", "url", "documentType", "title", "description")
                    data class Document(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: String,
                        @field:JsonProperty("url") @param:JsonProperty("url") val url: String,
                        @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,

                        @field:JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

                        @field:JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
                    )
                }
            }

            @JsonPropertyOrder("typeOfBuyer",
                               "mainGeneralActivity",
                               "mainSectoralActivity",
                               "gpaProfile",
                               "typeOfSupplier",
                               "mainEconomicActivities",
                               "scale",
                               "permits",
                               "bankAccounts",
                               "legalForm")
            data class Details(
                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("typeOfBuyer") @param:JsonProperty("typeOfBuyer") val typeOfBuyer: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("mainGeneralActivity") @param:JsonProperty("mainGeneralActivity") val mainGeneralActivity: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("mainSectoralActivity") @param:JsonProperty("mainSectoralActivity") val mainSectoralActivity: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("gpaProfile") @param:JsonProperty("gpaProfile") val gpaProfile: GpaProfile?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("typeOfSupplier") @param:JsonProperty("typeOfSupplier") val typeOfSupplier: String?,

                @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("mainEconomicActivities") @param:JsonProperty("mainEconomicActivities") val mainEconomicActivities: List<String>?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("scale") @param:JsonProperty("scale") val scale: String?,

                @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
                @field:JsonProperty("permits") @param:JsonProperty("permits") val permits: List<Permit>?,
                @field:JsonProperty("bankAccounts") @param:JsonProperty("bankAccounts") val bankAccounts: List<BankAccount>,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("legalForm") @param:JsonProperty("legalForm") val legalForm: LegalForm?
            ) {

                @JsonPropertyOrder("gpaAnnex", "gpaOrganizationType", "gpaThresholds")
                data class GpaProfile(
                    @field:JsonProperty("gpaAnnex") @param:JsonProperty("gpaAnnex") val gpaAnnex: GpaAnnex,
                    @field:JsonProperty("gpaOrganizationType") @param:JsonProperty("gpaOrganizationType") val gpaOrganizationType: GpaOrganizationType,
                    @field:JsonProperty("gpaThresholds") @param:JsonProperty("gpaThresholds") val gpaThresholds: List<GpaThreshold>
                ) {
                    data class GpaAnnex(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class GpaOrganizationType(
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class GpaThreshold(
                        @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String,
                        @field:JsonProperty("value") @param:JsonProperty("value") val value: Value
                    ) {
                        data class Value(
                            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,
                            @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
                        )
                    }
                }

                @JsonPropertyOrder("scheme", "id", "url", "permit")
                data class Permit(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("url") @param:JsonProperty("url") val url: String,
                    @field:JsonProperty("permit") @param:JsonProperty("permit") val permit: Permit
                ) {

                    @JsonPropertyOrder("issuedBy", "issuedThought", "validityPeriod")
                    data class Permit(
                        @field:JsonProperty("issuedBy") @param:JsonProperty("issuedBy") val issuedBy: IssuedBy,
                        @field:JsonProperty("issuedThought") @param:JsonProperty("issuedThought") val issuedThought: IssuedThought,
                        @field:JsonProperty("validityPeriod") @param:JsonProperty("validityPeriod") val validityPeriod: ValidityPeriod
                    ) {

                        @JsonPropertyOrder("id", "name")
                        data class IssuedBy(
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                        )

                        @JsonPropertyOrder("id", "name")
                        data class IssuedThought(
                            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                            @field:JsonProperty("name") @param:JsonProperty("name") val name: String
                        )

                        @JsonPropertyOrder("startDate", "endDate")
                        data class ValidityPeriod(
                            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String,
                            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: String
                        )
                    }
                }

                @JsonPropertyOrder("description",
                                   "bankName",
                                   "address",
                                   "identifier",
                                   "accountIdentification",
                                   "additionalAccountIdentifiers")
                data class BankAccount(
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                    @field:JsonProperty("bankName") @param:JsonProperty("bankName") val bankName: String,
                    @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
                    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,
                    @field:JsonProperty("accountIdentification") @param:JsonProperty("accountIdentification") val accountIdentification: AccountIdentification,
                    @field:JsonProperty("additionalAccountIdentifiers") @param:JsonProperty("additionalAccountIdentifiers") val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>
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

                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Country(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                            )

                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Region(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                            )

                            @JsonPropertyOrder("scheme", "id", "description", "uri")
                            data class Locality(
                                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                                @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

                                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                            )
                        }
                    }

                    @JsonPropertyOrder("scheme", "id")
                    data class Identifier(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                    )

                    @JsonPropertyOrder("scheme", "id")
                    data class AccountIdentification(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                    )

                    @JsonPropertyOrder("scheme", "id")
                    data class AdditionalAccountIdentifier(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String
                    )
                }

                @JsonPropertyOrder("scheme", "id", "description", "uri")
                data class LegalForm(
                    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                    @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                )
            }
        }

        @JsonPropertyOrder("id", "identifier", "title", "relationship", "scheme", "uri")
        data class RelatedProcess(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,
            @field:JsonProperty("relationship") @param:JsonProperty("relationship") val relationship: List<String>,
            @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
            @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
        )
    }
}