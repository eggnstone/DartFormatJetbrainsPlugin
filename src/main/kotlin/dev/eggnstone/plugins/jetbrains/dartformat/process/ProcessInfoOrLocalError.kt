package dev.eggnstone.plugins.jetbrains.dartformat.process

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException

sealed class ProcessInfoOrLocalError
{
    data class Normal(val processBuilderInfo: ProcessBuilderInfo) : ProcessInfoOrLocalError()
    data class LocalError(val error: DartFormatException) : ProcessInfoOrLocalError()
}
