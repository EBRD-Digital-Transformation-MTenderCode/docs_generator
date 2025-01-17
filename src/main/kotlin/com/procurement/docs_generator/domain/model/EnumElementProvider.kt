package com.procurement.docs_generator.domain.model

abstract class EnumElementProvider<T>(val info: EnumInfo<T>) where T : Enum<T>,
                                                                   T : EnumElementProvider.Key {

    @Target(AnnotationTarget.PROPERTY)
    annotation class DeprecatedElement

    @Target(AnnotationTarget.PROPERTY)
    annotation class ExcludedElement

    interface Key {
        val key: String
    }

    class EnumInfo<T>(
        val target: Class<T>,
        val values: Array<T>
    )

    companion object {
        inline fun <reified T : Enum<T>> info() = EnumInfo(target = T::class.java, values = enumValues())

        fun <T> Collection<T>.keysAsStrings(): List<String> where T : Enum<T>,
                                                                  T : Key = this
            .map { element -> element.key + if (element.isDeprecated()) " (Deprecated)" else "" }

        private fun <E : Enum<E>> Enum<E>.isNotExcluded(): Boolean = this.findAnnotation<ExcludedElement, E>() == null
        private fun <E : Enum<E>> Enum<E>.isDeprecated(): Boolean = this.findAnnotation<DeprecatedElement, E>() != null
        private inline fun <reified A : Annotation, E : Enum<E>> Enum<E>.findAnnotation(): A? = this.javaClass
            .getDeclaredField(this.name)
            .getAnnotation(A::class.java)
    }

    val allowedElements: List<T> = info.values.filter { element -> element.isNotExcluded() }

    private val elements: Map<String, T> = allowedElements.associateBy { it.key.toUpperCase() }

    fun orNull(key: String): T? = elements[key.toUpperCase()]

    fun orThrow(key: String): T = orNull(key)
        ?: throw EnumElementProviderException(
            enumType = info.target.canonicalName,
            value = key,
            values = info.values.joinToString { it.key }
        )

    operator fun contains(key: String): Boolean = orNull(key) != null
}
