package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class ResponseReader
{
    companion object
    {
        private const val WAIT_FOR_PROCESS_TO_FINISHED_INTERVAL_IN_MILLIS = 100L
        private const val WAIT_FOR_PROCESS_TO_FINISHED_TIMEOUT_IN_SECONDS = 5

        fun readResponse(process: Process, sendSomethingWhileWaiting: Boolean = false): JsonResponse
        {
            Logger.log("ResponseReader.readResponse()")

            val inputStream = process.inputStream
            val inputReader = inputStream.bufferedReader()
            val outputStream = process.outputStream
            val outputWriter = outputStream.bufferedWriter()
            val errorStream = process.errorStream
            val errorReader = errorStream.bufferedReader()

            var waitedMillis = 0L
            while (waitedMillis < WAIT_FOR_PROCESS_TO_FINISHED_TIMEOUT_IN_SECONDS * 1000L)
            {
                Logger.log("ExternalDartFormatter.readResponse() Loop")

                var readSome = false

                if (inputStream.available() > 0)
                {
                    val s = inputReader.readLine()
                    Logger.log("Received: $s")
                    readSome = true
                    return JsonResponse.fromOutputStream(s)
                }

                if (errorStream.available() > 0)
                {
                    val s = errorReader.readLine()
                    Logger.logError("Received error: $s")
                    readSome = true
                    return JsonResponse.fromErrorStream(s)
                }

                if (readSome)
                    continue

                if (sendSomethingWhileWaiting)
                {
                    Logger.log("ExternalDartFormatter.readResponse() Loop: Sending something")
                    outputWriter.write("\n")
                    outputWriter.flush()
                }

                if (process.waitFor(WAIT_FOR_PROCESS_TO_FINISHED_INTERVAL_IN_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS))
                    throw DartFormatException(FailType.ERROR, "Unexpected process exit")

                waitedMillis += WAIT_FOR_PROCESS_TO_FINISHED_INTERVAL_IN_MILLIS
            }

            throw DartFormatException(FailType.ERROR, "Timeout waiting for process to finish")
        }
    }
}
