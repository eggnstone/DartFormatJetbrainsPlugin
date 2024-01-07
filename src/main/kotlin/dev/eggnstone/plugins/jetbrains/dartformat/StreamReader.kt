package dev.eggnstone.plugins.jetbrains.dartformat

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.io.InputStream
import kotlin.math.min

class StreamReader(private val inputStream: InputStream)
{
    init
    {

    }

    fun readLine(): String
    {
        var s = ""

        while (true)
        {
            val c = inputStream.read()

            if (c == -1)
                break

            if (c == '\n'.code)
                break

            s += c.toChar()
        }

        return s
    }

    fun read(length: Int) : String
    {
        val buffer = ByteArray(length)

        var remainingLength = length
        var receivedLength = 0
        while (remainingLength > 0)
        {
            val l = min(remainingLength, 1000)
            Logger.log("Requesting $l bytes")
            val newlyReceivedLength = inputStream.read(buffer, receivedLength, l)
            Logger.log("Received: $newlyReceivedLength bytes")
            receivedLength += newlyReceivedLength
            remainingLength -= newlyReceivedLength
        }

        return String(buffer)
    }

    fun available(): Int
    {
        return inputStream.available()
    }
}