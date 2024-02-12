package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.data.WriteTask2
import kotlinx.coroutines.channels.Channel

class Writer
{
    private val channel = Channel<WriteTask2>()
    private val thread = Thread(WriterRunnable(channel))

    suspend fun send(writeTask: WriteTask2)
    {
        channel.send(writeTask)
    }

    fun shutdown()
    {
        channel.close()
        thread.interrupt()
    }
}
