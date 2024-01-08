package dev.eggnstone.plugins.jetbrains.dartformat

import java.io.InputStream

class StreamReader(private val inputStream: InputStream)
{
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

    fun available(): Int
    {
        return inputStream.available()
    }
}