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

    // Blocking drain until EOF. Use only after the process has exited so the pipe is closed;
    // otherwise this will block indefinitely. available() can return 0 right after exit even
    // when bytes remain, so we must read past it to surface stderr from crashed shim scripts.
    fun drainToEof(): String
    {
        val sb = StringBuilder()
        while (true)
        {
            val c = inputStream.read()
            if (c == -1)
                break

            sb.append(c.toChar())
        }

        return sb.toString()
    }
}
