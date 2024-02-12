package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.data.WriteTask2
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class T
{
    suspend fun f()
    {
        logDebug("T.f START")

        val writer = Writer()

        logDebug("T.f Calling sleep()")
        withContext(Dispatchers.IO) { Thread.sleep(500) }
        logDebug("T.f Called  sleep()")

        logDebug("T.f Calling writer.send()")
        writer.send(WriteTask2("X"))
        logDebug("T.f Called  writer.send()")

        logDebug("T.f Calling sleep()")
        withContext(Dispatchers.IO) { Thread.sleep(500) }
        logDebug("T.f Called  sleep()")

        logDebug("T.f Calling writer.shutdown()")
        writer.shutdown()
        logDebug("T.f Called  writer.shutdown()")

        logDebug("format END")
    }

    private fun logDebug(s: String)
    {
        Logger.logDebug(s)
    }
}