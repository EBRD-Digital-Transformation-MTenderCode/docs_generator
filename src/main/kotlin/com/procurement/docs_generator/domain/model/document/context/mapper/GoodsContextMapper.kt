package com.procurement.docs_generator.domain.model.document.context.mapper

import com.procurement.docs_generator.domain.model.document.context.GoodsContext
import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import java.time.LocalDate

object GoodsContextMapper {

    fun mapToContext(publishDate: LocalDate,
                     acRelease: ACReleasesPackage.Release,
                     evRelease: EVReleasesPackage.Release,
                     msRelease: MSReleasesPackage.Release): Map<String, Any> {
        val partyBuyer = acRelease.parties.partyByRole(role = "buyer")
        val partySupplier = acRelease.parties.partyByRole(role = "supplier")

        val ctx = GoodsContext(
            ac = GoodsContext.AC(
                date = acRelease.date.toLocalDate(),
                contract = acRelease.contracts[0].let { contract ->
                    GoodsContext.AC.Contract(
                        id = contract.id,
                        description = contract.description,
                        value = contract.value.amount.toDouble(),
                        endDate = contract.period.endDate.toLocalDate(),
                        agreedMetrics = getContractAgreedMetrics(acRelease)
                    )
                },
                tender = acRelease.tender.let { tender ->
                    GoodsContext.AC.Tender(
                        procurementMethodDetails = tender.procurementMethodDetails,
                        classification = tender.classification.let { classification ->
                            GoodsContext.AC.Tender.Classification(
                                id = classification.id,
                                description = classification.description
                            )
                        }
                    )
                },
                buyer = partyBuyer.let { party ->
                    GoodsContext.AC.Buyer(
                        address = party.address.let { address ->
                            GoodsContext.AC.Buyer.Address(
                                country = address.addressDetails.country.description,
                                region = address.addressDetails.region.description,
                                locality = address.addressDetails.locality.description,
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode
                            )
                        },
                        identifier = party.identifier.let { identifier ->
                            GoodsContext.AC.Buyer.Identifier(
                                id = identifier.id,
                                legalName = identifier.legalName
                            )
                        },
                        additionalIdentifiers = party.mapAdditionalIdentifiersByScheme(scheme = "MD-FISCAL") {
                            GoodsContext.AC.Buyer.AdditionalIdentifier(id = it.id)
                        },
                        contactPoint = GoodsContext.AC.Buyer.ContactPoint(
                            telephone = party.contactPoint.telephone
                        ),
                        persones = party.persones?.map { person ->
                            GoodsContext.AC.Buyer.Person(
                                title = person.title,
                                name = person.name,
                                businessFunctions = person.businessFunctions.mapBusinessFunctionByType(type = "authority") {
                                    GoodsContext.AC.Buyer.Person.BusinessFunction(
                                        jobTitle = it.jobTitle
                                    )
                                }
                            )
                        } ?: emptyList(),
                        details = party.details?.let { detail ->
                            GoodsContext.AC.Buyer.Details(
                                bankAccount = detail.bankAccounts[0].let { bankAccount ->
                                    GoodsContext.AC.Buyer.Details.BankAccounts(
                                        accountIdentification = bankAccount.accountIdentification.id,
                                        identifier = bankAccount.identifier.id,
                                        name = bankAccount.bankName,
                                        address = bankAccount.address.let { address ->
                                            GoodsContext.AC.Buyer.Details.BankAccounts.Address(
                                                country = address.addressDetails.country.description,
                                                region = address.addressDetails.region.description,
                                                locality = address.addressDetails.locality.description,
                                                streetAddress = address.streetAddress,
                                                postalCode = address.postalCode
                                            )
                                        }
                                    )
                                },
                                legalForm = GoodsContext.AC.Buyer.Details.LegalForm(
                                    detail.legalForm?.description
                                        ?: throw IllegalStateException("Party -> buyer -> details no contains 'legalForm'.")
                                ),
                                permit = detail.permits.firstPermitByScheme(scheme = "MD-SRLE") { permit ->
                                    GoodsContext.AC.Buyer.Details.Permit(
                                        id = permit.id,
                                        startDate = permit.permit.validityPeriod.startDate.toLocalDate()
                                    )
                                }
                            )
                        } ?: throw IllegalStateException("Party of the buyer no contains 'details'.")

                    )
                },
                supplier = partySupplier.let { party ->
                    GoodsContext.AC.Supplier(
                        address = party.address.let { address ->
                            GoodsContext.AC.Supplier.Address(
                                country = address.addressDetails.country.description,
                                region = address.addressDetails.region.description,
                                locality = address.addressDetails.locality.description,
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode
                            )
                        },
                        identifier = party.identifier.let { identifier ->
                            GoodsContext.AC.Supplier.Identifier(
                                id = identifier.id,
                                legalName = identifier.legalName
                            )
                        },
                        additionalIdentifiers = party.mapAdditionalIdentifiersByScheme(scheme = "MD-FISCAL") {
                            GoodsContext.AC.Supplier.AdditionalIdentifier(id = it.id)
                        },
                        contactPoint = GoodsContext.AC.Supplier.ContactPoint(
                            telephone = party.contactPoint.telephone
                        ),
                        persones = party.persones?.map { person ->
                            GoodsContext.AC.Supplier.Person(
                                title = person.title,
                                name = person.name,
                                businessFunctions = person.businessFunctions.mapBusinessFunctionByType(type = "authority") { businessFunction ->
                                    GoodsContext.AC.Supplier.Person.BusinessFunction(
                                        jobTitle = businessFunction.jobTitle,
                                        documents = businessFunction.documents.mapDocumentsByDocumentType(documentType = "regulatoryDocument") {
                                            GoodsContext.AC.Supplier.Person.BusinessFunction.Document(
                                                title = it.title!!
                                            )
                                        }
                                    )
                                }
                            )
                        } ?: emptyList(),
                        details = party.details?.let { detail ->
                            GoodsContext.AC.Supplier.Details(
                                bankAccount = detail.bankAccounts[0].let { bankAccount ->
                                    GoodsContext.AC.Supplier.Details.BankAccounts(
                                        accountIdentification = bankAccount.accountIdentification.id,
                                        identifier = bankAccount.identifier.id,
                                        name = bankAccount.bankName,
                                        address = bankAccount.address.let { address ->
                                            GoodsContext.AC.Supplier.Details.BankAccounts.Address(
                                                country = address.addressDetails.country.description,
                                                region = address.addressDetails.region.description,
                                                locality = address.addressDetails.locality.description,
                                                streetAddress = address.streetAddress,
                                                postalCode = address.postalCode
                                            )
                                        }
                                    )
                                },
                                legalForm = GoodsContext.AC.Supplier.Details.LegalForm(
                                    description = detail.legalForm?.description
                                        ?: throw IllegalStateException("Party -> supplier -> details no contains 'legalForm'.")
                                ),
                                permit = detail.permits.firstPermitByScheme(scheme = "MD-SRLE") { permit ->
                                    GoodsContext.AC.Supplier.Details.Permit(
                                        id = permit.id,
                                        startDate = permit.permit.validityPeriod.startDate.toLocalDate()
                                    )
                                }
                            )
                        } ?: throw IllegalStateException("Party of the supplier no contains 'details'.")

                    )
                },
                award = acRelease.awards.firstOrNull { award ->
                    award.relatedLots[0] == acRelease.tender.lots[0].id
                }?.let { award ->
                    GoodsContext.AC.Award(
                        date = award.date.toLocalDate(),
                        relatedLot = GoodsContext.AC.Award.RelatedLot(id = award.relatedLots[0]),
                        items = award.items.map { item ->
                            GoodsContext.AC.Award.Item(
                                classification = item.classification.let { classification ->
                                    GoodsContext.AC.Award.Item.Classification(
                                        id = classification.id,
                                        description = classification.description
                                    )
                                },
                                description = item.description,
                                unit = item.unit.let { unit ->
                                    GoodsContext.AC.Award.Item.Unit(
                                        name = unit.name,
                                        value = unit.value.let { value ->
                                            GoodsContext.AC.Award.Item.Unit.Value(
                                                amount = value.amount.toDouble(),
                                                amountNet = value.amountNet.toDouble()
                                            )
                                        }
                                    )
                                },
                                planning = acRelease.planning.budget.let { budget ->
                                    GoodsContext.AC.Award.Item.Planning(
                                        budgetAllocation = budget.budgetAllocation.firstOrNull { budgetAllocation ->
                                            budgetAllocation.relatedItem == item.id
                                        }?.let { budgetAllocation ->
                                            GoodsContext.AC.Award.Item.Planning.BudgetAllocation(
                                                period = budgetAllocation.period.let { period ->
                                                    GoodsContext.AC.Award.Item.Planning.BudgetAllocation.Period(
                                                        startDate = period.startDate.toLocalDate(),
                                                        endDate = period.endDate.toLocalDate()
                                                    )
                                                },
                                                budgetBreakdownID = budgetAllocation.budgetBreakdownID
                                            )
                                        }
                                            ?: throw IllegalStateException("BudgetAllocation by relatedItem: '${item.id}' not found.")

                                    )
                                },
                                quantity = item.quantity.toDouble(),
                                agreedMetrics = getItemAgreedMetrics(awardId = award.id,
                                                                     itemId = item.id,
                                                                     release = acRelease)
                            )
                        }
                    )
                } ?: throw IllegalStateException("Award not found.")
            ),
            ev = GoodsContext.EV(
                publishDate = publishDate,
                tender = evRelease.tender.let { tender ->
                    GoodsContext.EV.Tender(
                        id = tender.id
                    )
                }
            ),
            ms = GoodsContext.MS(
                tender = msRelease.tender.let { tender ->
                    GoodsContext.MS.Tender(
                        id = tender.id,
                        title = tender.title
                    )
                }
            )
        )

        return mutableMapOf<String, Any>().apply {
            this["context"] = ctx
        }
    }

    private fun getContractAgreedMetrics(release: ACReleasesPackage.Release): GoodsContext.AC.Contract.AgreedMetrics {
        val metrics = ContractAgreedMetrics(
            mutableMapOf<String, String>().apply {
                for (metric in release.contracts[0].agreedMetrics) {
                    if (metric.id.startsWith("cc-general")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-general-1-1" -> this["ccGeneral_1_1Measure"] = it.measure.toString()
                                "cc-general-1-2" -> this["ccGeneral_1_2Measure"] = it.measure.toString()
                                "cc-general-1-3" -> this["ccGeneral_1_3Measure"] = it.measure.toString()
                            }
                        }
                    }

                    if (metric.id.startsWith("cc-buyer")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-buyer-1-1" -> this["ccBuyer_1_1Measure"] = it.measure.toString()
                                "cc-buyer-2-1" -> this["ccBuyer_2_1Measure"] = it.measure.toString()
                                "cc-buyer-2-2" -> this["ccBuyer_2_2Measure"] = it.measure.toString()
                            }
                        }
                    }

                    if (metric.id.startsWith("cc-tenderer")) {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-tenderer-1-1" -> this["ccTenderer_1_1Measure"] = it.measure.toString()
                                "cc-tenderer-1-2" -> this["ccTenderer_1_2Measure"] = it.measure.toString()
                                "cc-tenderer-2-1" -> this["ccTenderer_2_1Measure"] = it.measure.toString()
                                "cc-tenderer-2-2" -> this["ccTenderer_2_2Measure"] = it.measure.toString()
                                "cc-tenderer-2-3" -> this["ccTenderer_2_3Measure"] = it.measure.toString()
                                "cc-tenderer-2-4" -> this["ccTenderer_2_4Measure"] = it.measure.toString()
                                "cc-tenderer-3-1" -> this["ccTenderer_3_1Measure"] = it.measure.toString()
                                "cc-tenderer-3-2" -> this["ccTenderer_3_2Measure"] = it.measure.toString()
                                "cc-tenderer-3-3" -> this["ccTenderer_3_3Measure"] = it.measure.toString()
                            }
                        }
                    }
                }
            }
        )

        return GoodsContext.AC.Contract.AgreedMetrics(
            ccGenerel_1_1Measure = metrics.ccGeneral_1_1Measure,
            ccGenerel_1_2Measure = metrics.ccGeneral_1_2Measure,
            ccGenerel_1_3Measure = metrics.ccGeneral_1_3Measure,

            ccBuyer_1_1Measure = metrics.ccBuyer_1_1Measure,
            ccBuyer_2_1Measure = metrics.ccBuyer_2_1Measure,
            ccBuyer_2_2Measure = metrics.ccBuyer_2_2Measure,

            ccTenderer_1_1Measure = metrics.ccTenderer_1_1Measure,
            ccTenderer_1_2Measure = metrics.ccTenderer_1_2Measure,
            ccTenderer_2_1Measure = metrics.ccTenderer_2_1Measure,
            ccTenderer_2_2Measure = metrics.ccTenderer_2_2Measure,
            ccTenderer_2_3Measure = metrics.ccTenderer_2_3Measure,
            ccTenderer_2_4Measure = metrics.ccTenderer_2_4Measure,
            ccTenderer_3_1Measure = metrics.ccTenderer_3_1Measure,
            ccTenderer_3_2Measure = metrics.ccTenderer_3_2Measure,
            ccTenderer_3_3Measure = metrics.ccTenderer_3_3Measure
        )
    }

    private fun getItemAgreedMetrics(awardId: String,
                                     itemId: String,
                                     release: ACReleasesPackage.Release): GoodsContext.AC.Award.Item.AgreedMetrics {
        val ccSubject = "cc-subject-$awardId-$itemId"
        for (metric in release.contracts[0].agreedMetrics) {

            if (metric.id == ccSubject) {
                val metrics = ItemAgreedMetrics(
                    mutableMapOf<String, String>().apply {
                        metric.observations.forEach {
                            when (it.id) {
                                "cc-subject-1" -> this["ccSubject_1Measure"] = it.measure.toString()
                                "cc-subject-2" -> this["ccSubject_2Measure"] = it.measure.toString()
                                "cc-subject-3" -> this["ccSubject_3Measure"] = it.measure.toString()
                                "cc-subject-4" -> this["ccSubject_4Measure"] = it.measure.toString()
                                "cc-subject-5" -> this["ccSubject_5Measure"] = it.measure.toString()
                            }
                        }
                    }
                )

                return GoodsContext.AC.Award.Item.AgreedMetrics(
                    ccSubject_1Measure = metrics.ccSubject_1Measure,
                    ccSubject_2Measure = metrics.ccSubject_2Measure,
                    ccSubject_3Measure = metrics.ccSubject_3Measure,
                    ccSubject_4Measure = metrics.ccSubject_4Measure,
                    ccSubject_5Measure = metrics.ccSubject_5Measure
                )
            }
        }

        throw IllegalStateException("Contract not contains '$ccSubject'")
    }

    private class ContractAgreedMetrics(props: Map<String, String>) {
        val ccGeneral_1_1Measure: String by props
        val ccGeneral_1_2Measure: String by props
        val ccGeneral_1_3Measure: String by props

        val ccBuyer_1_1Measure: String by props
        val ccBuyer_2_1Measure: String by props
        val ccBuyer_2_2Measure: String by props

        val ccTenderer_1_1Measure: String by props
        val ccTenderer_1_2Measure: String by props
        val ccTenderer_2_1Measure: String by props
        val ccTenderer_2_2Measure: String by props
        val ccTenderer_2_3Measure: String by props
        val ccTenderer_2_4Measure: String by props
        val ccTenderer_3_1Measure: String by props
        val ccTenderer_3_2Measure: String by props
        val ccTenderer_3_3Measure: String by props
    }

    private class ItemAgreedMetrics(props: Map<String, String>) {
        val ccSubject_1Measure: String by props
        val ccSubject_2Measure: String by props
        val ccSubject_3Measure: String by props
        val ccSubject_4Measure: String by props
        val ccSubject_5Measure: String by props
    }
}