package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class Whitespace(val text: String) : IPart
{
    /*override fun equals(other: Any?): Boolean = other is Whitespace && text == other.text

    override fun hashCode(): Int = text.hashCode()*/

    override fun recreate(): String = text

    override fun toString(): String = "Whitespace(${Tools.toDisplayString(text)})"
}
