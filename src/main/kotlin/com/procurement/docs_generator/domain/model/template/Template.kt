package com.procurement.docs_generator.domain.model.template

import com.procurement.docs_generator.domain.model.document.mode.Mode
import java.nio.ByteBuffer
import java.time.LocalDate

data class Template(
    val startDate: LocalDate,
    val mode: Mode,
    val body: ByteBuffer
)