package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.data.WriteTask2
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking

class WriterRunnable(private val channel: Channel<WriteTask2>) : Runnable
{
    override fun run()
    {
        logDebug("WriterRunnable.run START")

        try
        {
            while (true)
            {
                logDebug("WriterRunnable.run: Calling receive()")
                val task = runBlocking { channel.receive() }
                logDebug("WriterRunnable.run: Called  receive()")
                logDebug("WriterRunnable.run: Calling task.execute()")
                task.execute()
                logDebug("WriterRunnable.run: Called  task.execute()")
            }
        }
        catch (e: InterruptedException)
        {
            logDebug("WriterRunnable.run InterruptedException")
        }

        logDebug("WriterRunnable.run END")
    }

    private fun logDebug(s: String)
    {
        Logger.logDebug(s)
    }
}

