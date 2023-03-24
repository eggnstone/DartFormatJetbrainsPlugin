package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.Tools

data class Comment(val text: String, val startPos: Int = 0) : IPart
{
    override fun recreate(): String = text

    override fun toString(): String = "Comment(${Tools.toDisplayString(text)}, $startPos)"
}