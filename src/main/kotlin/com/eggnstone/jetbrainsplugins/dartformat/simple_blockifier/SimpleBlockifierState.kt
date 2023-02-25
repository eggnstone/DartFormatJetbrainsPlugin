package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.ISimpleBlock

class SimpleBlockifierState
{
    val blocks = arrayListOf<ISimpleBlock>()

    var currentAreaType = SimpleAreaType.Unknown
    var currentText = ""
}
