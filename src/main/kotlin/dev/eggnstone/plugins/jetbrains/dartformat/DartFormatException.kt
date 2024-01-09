package dev.eggnstone.plugins.jetbrains.dartformat

@kotlinx.serialization.Serializable
data class DartFormatException(
    val type: FailType,
    val source: ExceptionSourceType,
    override val message: String,
    override val cause: Throwable? = null,
    val line: Int? = null,
    val column: Int? = null
) : Exception()
