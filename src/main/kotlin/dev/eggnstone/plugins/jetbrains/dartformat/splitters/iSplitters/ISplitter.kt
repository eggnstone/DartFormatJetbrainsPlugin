package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

interface ISplitter
{
    val name: String

    fun split(inputText: String, params: SplitParams = SplitParams()): SplitResult
}
