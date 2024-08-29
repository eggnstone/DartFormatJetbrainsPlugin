package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException

class ExternalDartFormatInfo(
    val localError: DartFormatException? = null,
    val executable: String? = null,
    val additionalParam: String? = null,
    val additionalParam2: String? = null
)
{
    companion object
    {
        fun exception(localError: DartFormatException): ExternalDartFormatInfo
        {
            return ExternalDartFormatInfo(localError = localError)
        }

        fun normal(executable: String): ExternalDartFormatInfo
        {
            return ExternalDartFormatInfo(executable = executable)
        }

        fun withAdditionalParam(executable: String, additionalParam: String): ExternalDartFormatInfo
        {
            return ExternalDartFormatInfo(executable = executable, additionalParam = additionalParam)
        }

        fun withAdditionalParams(executable: String, additionalParam: String, additionalParam2: String): ExternalDartFormatInfo
        {
            return ExternalDartFormatInfo(executable = executable, additionalParam = additionalParam, additionalParam2 = additionalParam2)
        }
    }
}
