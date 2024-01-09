package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.*
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class TimedReader
{
    companion object
    {
        fun readLine(process: Process, inputReader: StreamReader, timeoutInSeconds: Int): String
        {
            Logger.log("TimedReader.readLine()")

            var waitedMillis = 0
            while (waitedMillis < timeoutInSeconds * 1000)
            {
                val textFromInputStream = receiveLine(inputReader)
                if (textFromInputStream != null)
                    return textFromInputStream

                if (process.waitFor(Constants.WAIT_INTERVAL_IN_MILLIS.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS))
                {
                    val errorText = "Unexpected process exit."
                    Logger.logError("TimedReader.readResponse: $errorText")
                    throw DartFormatException(FailType.Error, ExceptionSourceType.Local, errorText)
                }

                Thread.sleep(Constants.WAIT_INTERVAL_IN_MILLIS.toLong())
                waitedMillis += Constants.WAIT_INTERVAL_IN_MILLIS
            }

            Logger.log("TimedReader.readResponse: waitedMillis: $waitedMillis")

            val errorText = "Timeout while waiting for response."
            Logger.logError("TimedReader.readLine: $errorText")
            throw DartFormatException(FailType.Error, ExceptionSourceType.Local, errorText)
        }

        private fun receiveLine(streamReader: StreamReader): String?
        {
            val availableBytes = streamReader.available()
            if (availableBytes <= 0)
                return null

            Logger.log("TimedReader.receiveLine: Receiving: $availableBytes bytes.")
            return streamReader.readLine()
        }
    }
}
