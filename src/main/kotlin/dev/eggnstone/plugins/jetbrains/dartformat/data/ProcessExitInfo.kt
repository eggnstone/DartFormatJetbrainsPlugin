package dev.eggnstone.plugins.jetbrains.dartformat.data

data class ProcessExitInfo(val stdOutTail: String, val stdErrTail: String, val exitCode: Int)
{
    companion object
    {
        // Folds lines that were already consumed by a read loop together with the tail captured
        // at process exit, so callers can hand one ProcessExitInfo to a shared notification helper.
        fun combine(stdOutLines: String?, stdErrLines: String?, exitInfo: ProcessExitInfo?): ProcessExitInfo
        {
            val outParts = listOfNotNull(stdOutLines?.ifEmpty { null }, exitInfo?.stdOutTail?.ifEmpty { null })
            val errParts = listOfNotNull(stdErrLines?.ifEmpty { null }, exitInfo?.stdErrTail?.ifEmpty { null })
            return ProcessExitInfo(
                stdOutTail = outParts.joinToString("\n"),
                stdErrTail = errParts.joinToString("\n"),
                exitCode = exitInfo?.exitCode ?: -1
            )
        }
    }
}
