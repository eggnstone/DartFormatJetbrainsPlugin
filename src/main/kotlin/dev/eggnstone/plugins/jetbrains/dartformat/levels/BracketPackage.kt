package dev.eggnstone.plugins.jetbrains.dartformat.levels

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

data class BracketPackage(val brackets: List<String>, val lineIndex: Int)
{
    override fun toString(): String = "BracketPackage(lineIndex=$lineIndex, brackets=${Tools.toDisplayStringForStrings(brackets)})"
}
