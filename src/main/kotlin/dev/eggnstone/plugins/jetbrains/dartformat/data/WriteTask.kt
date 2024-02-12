package dev.eggnstone.plugins.jetbrains.dartformat.data

data class WriteTask(
    val virtualFileEx: VirtualFileEx,
    val text: String
)
