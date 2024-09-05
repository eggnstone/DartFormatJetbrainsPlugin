package dev.eggnstone.plugins.jetbrains.dartformat.process

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException

class ProcessInfoOrException(
    val localError: DartFormatException? = null,
    val processBuilderInfo: ProcessBuilderInfo? = null
)
{
    companion object
    {
        fun exception(localError: DartFormatException): ProcessInfoOrException
        {
            return ProcessInfoOrException(localError = localError)
        }

        fun normal(processBuilderInfo: ProcessBuilderInfo): ProcessInfoOrException
        {
            return ProcessInfoOrException(processBuilderInfo = processBuilderInfo)
        }
    }
}
