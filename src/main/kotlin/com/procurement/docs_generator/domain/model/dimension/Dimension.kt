package com.procurement.docs_generator.domain.model.dimension

private const val kbytes: Long = 1024
private const val mbytes: Long = 1024 * 1024
private const val gbytes: Long = 1024 * 1024 * 1024

fun Int.toKb() = this * kbytes
fun Int.toMb() = this * mbytes
fun Int.toGb() = this * gbytes

fun Long.toKb() = this * kbytes
fun Long.toMb() = this * mbytes
fun Long.toGb() = this * gbytes