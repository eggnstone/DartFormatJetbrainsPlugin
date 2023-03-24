package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class Statement(val text: String) : IPart
{
    override fun recreate(): String = text

    override fun toString(): String = "Statement(${Tools.toDisplayString(text)})"
}
