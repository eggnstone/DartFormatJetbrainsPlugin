package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.enums.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.enums.FailType
import kotlinx.serialization.Serializable

@Serializable
data class DartFormatException(
    val type: FailType,
    val source: ExceptionSourceType,
    override val message: String,
    //override val cause: Throwable? = null,
    //override val cause: String? = null,
    val line: Int? = null,
    val column: Int? = null
) : Exception()
{
    companion object
    {
        fun localError(message: String, /*cause: Throwable? = null,*/ line: Int? = null, column: Int? = null): DartFormatException
        {
            return DartFormatException(FailType.Error, ExceptionSourceType.Local, message, /*cause,*/ line, column)
        }
    }
}
