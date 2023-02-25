package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.IBlock

class BlockifierStateOld
{
    val blocks = mutableListOf<IBlock>()

    var currentClassHeader = ""
    var currentText = ""
    var currentType = AreaType.Unknown
}
