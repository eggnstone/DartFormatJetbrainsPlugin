package dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

data class SplitResult(val remainingText: String, val parts: List<IPart>)
