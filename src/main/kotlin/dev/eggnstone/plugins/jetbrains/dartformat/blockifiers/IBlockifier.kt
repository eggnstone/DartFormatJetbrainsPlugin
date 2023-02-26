package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

interface IBlockifier
{
    fun blockify(inputText: String): BlockifyResult
}
