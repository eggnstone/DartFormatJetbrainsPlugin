package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.data.WriteTask2
import kotlinx.coroutines.channels.Channel

class WriterThread(channel: Channel<WriteTask2>) : Thread()
{
    private val channel = Channel<WriteTask2>()

    suspend fun send(writeTask2: WriteTask2)
    {
        channel.send(writeTask2)
    }

    suspend fun inter()
    {
        channel.close()
    }
}

