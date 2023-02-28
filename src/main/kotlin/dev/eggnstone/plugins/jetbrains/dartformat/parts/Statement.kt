package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

class Statement(val text: String) : IPart
{
    override fun equals(other: Any?): Boolean = other is Statement && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "Statement(${Tools.toDisplayString(text)})"
}
