package dev.eggnstone.plugins.jetbrains.dartformat.parts

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

data class Comment(val text: String, val startPos: Int = 0) : IPart
{
    override fun recreate(): String = text

    override fun toString(): String = "Comment(startPos=$startPos, ${Tools.toDisplayString(text)})"
}
