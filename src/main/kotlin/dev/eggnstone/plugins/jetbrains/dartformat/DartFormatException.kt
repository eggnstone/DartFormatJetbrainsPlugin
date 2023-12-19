package dev.eggnstone.plugins.jetbrains.dartformat

class DartFormatException(isBug: Boolean, message: String) : Exception(message)
{
    val isBug get() = mIsBug

    private val mIsBug = isBug
}
