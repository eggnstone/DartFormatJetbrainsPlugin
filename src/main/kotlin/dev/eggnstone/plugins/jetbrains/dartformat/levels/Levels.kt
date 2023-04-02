package dev.eggnstone.plugins.jetbrains.dartformat.levels

data class Levels(
    val newConditionals: Int,
    val newClosedConditionals: Int,
    val newBracketPackages: List<BracketPackage>,
    val isElse: Boolean,
    val isInSquareBracketIf: Boolean,
    val isInMultiLineComment: Boolean
)
