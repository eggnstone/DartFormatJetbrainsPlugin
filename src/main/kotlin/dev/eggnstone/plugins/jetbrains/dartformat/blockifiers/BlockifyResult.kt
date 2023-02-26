package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

data class BlockifyResult(val remainingText: String, val parts: List<IPart>)