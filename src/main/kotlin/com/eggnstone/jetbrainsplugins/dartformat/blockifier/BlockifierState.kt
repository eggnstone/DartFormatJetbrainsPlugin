package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.BlockType
import com.eggnstone.jetbrainsplugins.dartformat.blocks.IBlock

class BlockifierState
{
    val blocks = arrayListOf<IBlock>()

    var currentClassHeader = ""
    var currentText = ""
    var currentType = BlockType.Unknown
}
