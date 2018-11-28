package com.procurement.docs_generator.domain.model.document.context.mapper

import com.procurement.docs_generator.domain.model.release.ACReleasesPackage
import com.procurement.docs_generator.domain.model.release.EVReleasesPackage
import com.procurement.docs_generator.domain.model.release.MSReleasesPackage
import java.time.LocalDate

object WorksContextMapper {

    fun mapToContext(publishDate: LocalDate,
                     acRelease: ACReleasesPackage.Release,
                     evRelease: EVReleasesPackage.Release,
                     msRelease: MSReleasesPackage.Release): Map<String, Any> {
        TODO()
    }
}