package dev.eggnstone.plugins.jetbrains.dartformat.splitters

data class TypeSplitterType(val name: String, val useNextChar: Boolean, val function: (String, String?) -> Boolean, val combineSimilar: Boolean)
