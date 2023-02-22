package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Block(val tokens:  List<IToken>) :IBlock
{
    override fun equals(other: Any?): Boolean = other is Block && tokens == other.tokens

    override fun hashCode(): Int = tokens.hashCode()

    override fun toString(): String = "Block(${Tools.toString(tokens)})"
}
