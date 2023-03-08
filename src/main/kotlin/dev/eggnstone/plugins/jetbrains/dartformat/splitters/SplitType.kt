package dev.eggnstone.plugins.jetbrains.dartformat.splitters

data class SplitType(val name: String, val function: (String) -> Boolean, val combineSame: Boolean)
