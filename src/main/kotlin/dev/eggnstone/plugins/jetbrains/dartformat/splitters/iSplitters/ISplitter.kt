package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

interface ISplitter
{
    fun split(inputText: String): SplitResult
}
