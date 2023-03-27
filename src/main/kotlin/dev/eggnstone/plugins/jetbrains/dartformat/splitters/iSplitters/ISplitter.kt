package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

interface ISplitter
{
    val name: String

    fun split(inputText: String, params: SplitParams = SplitParams(), inputCurrentIndent: Int = 0, stopAfter: Int = -1): SplitResult
}
