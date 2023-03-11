package dev.eggnstone.plugins.jetbrains.dartformat.splitters

data class TypeSplitterType(val name: String, val function: (String) -> Boolean, val combineSimilar: Boolean)