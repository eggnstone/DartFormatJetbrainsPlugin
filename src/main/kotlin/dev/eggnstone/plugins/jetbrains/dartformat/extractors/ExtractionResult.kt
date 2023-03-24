package dev.eggnstone.plugins.jetbrains.dartformat.extractors

data class ExtractionResult(val comment: String, val startPos: Int, val remainingText: String)
