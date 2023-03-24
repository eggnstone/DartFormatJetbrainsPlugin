package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class Whitespace(val text: String) : IPart
{
    override fun recreate(): String = text

    override fun toString(): String = "Whitespace(${Tools.toDisplayString(text)})"
}
