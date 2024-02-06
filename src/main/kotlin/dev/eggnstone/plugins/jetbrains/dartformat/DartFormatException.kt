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
{
    companion object
    {
        fun localError(message: String, cause: Throwable? = null, line: Int? = null, column: Int? = null): DartFormatException
        {
            return DartFormatException(FailType.Error, ExceptionSourceType.Local, message, cause, line, column)
        }
    }
}
