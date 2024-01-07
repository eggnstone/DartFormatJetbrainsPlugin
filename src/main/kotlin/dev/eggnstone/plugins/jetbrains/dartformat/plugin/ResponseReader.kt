package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.*
import dev.eggnstone.plugins.jetbrains.dartformat.json.JsonResponse
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.io.InputStream

class ResponseReader
{
    companion object
    {
        fun readResponse(process: Process, inputReader: StreamReader, errorReader: StreamReader): JsonResponse
        {
            Logger.log("ResponseReader.readResponse()")

            var waitedMillis = 0L
            while (waitedMillis < Constants.WAIT_FOR_READ_RESPONSE_IN_SECONDS * 1000L)
            {
                //Logger.log("ResponseReader.readResponse: loop")

                val textFromInputStream = receiveLine(inputReader, "inputStream")
                if (textFromInputStream != null)
                    return JsonResponse.fromInputStream(textFromInputStream)

                //Logger.log("ResponseReader.readResponse: 1")

                val textFromErrorStream = receiveLine(errorReader, "errorStream")
                if (textFromErrorStream != null)
                    return JsonResponse.fromErrorStream(textFromErrorStream)

                //Logger.log("ResponseReader.readResponse: 2")

                if (process.waitFor(Constants.WAIT_INTERVAL_IN_MILLIS.toLong(), java.util.concurrent.TimeUnit.MILLISECONDS))
                {
                    val errorText = "Unexpected process exit."
                    Logger.logError("ResponseReader.readResponse: $errorText")
                    throw DartFormatException(FailType.ERROR, errorText)
                }

                waitedMillis += Constants.WAIT_INTERVAL_IN_MILLIS
            }

            val errorText = "Timeout while waiting for response."
            Logger.logError("ResponseReader.readResponse: $errorText")
            throw DartFormatException(FailType.ERROR, errorText)
        }

        private fun receiveAllLines(stream: InputStream, name: String): String?
        {
            Logger.log("ResponseReader.receiveAllLines($name)")

            val availableBytes = stream.available()
            if (availableBytes <= 0)
                return null

            Logger.log("ResponseReader.receiveAllLines: Receiving: $availableBytes bytes from $name ...")

            var byteArray: ByteArray
            try
            {
                Logger.log("ResponseReader.receiveAllLines: Calling stream.readNBytes($availableBytes) ...")
                byteArray = stream.readNBytes(availableBytes)
                Logger.log("ResponseReader.receiveAllLines: Called stream.readNBytes($availableBytes).")
            }
            catch (e: Exception)
            {
                Logger.logError("ResponseReader.receiveAllLines: Exception: ${e.message}")
                byteArray = ByteArray(0)
            }
            /*catch (e: Error)
            {
                Logger.logError("ResponseReader.receiveAllLines: Error: ${e.message}")
                byteArray = ByteArray(0)
            }*/

            Logger.log("ResponseReader.receiveAllLines: Received ${byteArray.size} bytes.")
            val s = byteArray.decodeToString()
            Logger.log("ResponseReader.receiveAllLines: Received ${s.length} \"$s\"")

            return s
        }

        private fun receiveLine(streamReader: StreamReader, name: String): String?
        {
            //Logger.log("ResponseReader.receiveLine($name)")

            val availableBytes = streamReader.available()
            if (availableBytes <= 0)
                return null

            Logger.log("ResponseReader.receiveLine: Receiving: $availableBytes bytes from $name ...")
            return streamReader.readLine()
        }
    }
}
