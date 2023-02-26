package dev.eggnstone.plugins.jetbrains.dartformat.splitters

interface ISplitter
{
    fun split(inputText: String): SplitResult
}
