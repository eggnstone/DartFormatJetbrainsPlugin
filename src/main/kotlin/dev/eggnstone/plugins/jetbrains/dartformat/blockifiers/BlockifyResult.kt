package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock

data class BlockifyResult(val remainingText: String, val block: IBlock)