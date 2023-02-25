package com.eggnstone.jetbrainsplugins.dartformat.simple_blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.ISimpleBlock

class SimpleBlockifierState
{
    val blocks = arrayListOf<ISimpleBlock>()

    var currentAreaType: SimpleAreaType = SimpleAreaType.Unknown
        private set

    var currentCurlyBracketCount: Int = 0
    var currentText: String = ""

    fun reset(areaType: SimpleAreaType, text: String)
    {
        currentAreaType = areaType
        currentCurlyBracketCount = 0
        this.currentText = text
    }
}
