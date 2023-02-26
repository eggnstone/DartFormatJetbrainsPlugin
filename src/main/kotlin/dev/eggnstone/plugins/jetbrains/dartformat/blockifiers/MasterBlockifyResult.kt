package dev.eggnstone.plugins.jetbrains.dartformat.blockifiers

import dev.eggnstone.plugins.jetbrains.dartformat.blocks.IBlock

data class MasterBlockifyResult(val remainingText: String, val blocks: List<IBlock>)
