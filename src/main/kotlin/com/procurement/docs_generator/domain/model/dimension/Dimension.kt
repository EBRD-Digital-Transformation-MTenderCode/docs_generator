package com.procurement.docs_generator.domain.model.dimension

private const val kbytes: Long = 1024
private const val mbytes: Long = 1024 * 1024
private const val gbytes: Long = 1024 * 1024 * 1024

val Int.Kb: Long
    get() = this * kbytes
val Long.Kb: Long
    get() = this * kbytes

val Int.Mb: Long
    get() = this * mbytes
val Long.Mb: Long
    get() = this * mbytes

val Int.Gb: Long
    get() = this * gbytes
val Long.Gb: Long
    get() = this * gbytes