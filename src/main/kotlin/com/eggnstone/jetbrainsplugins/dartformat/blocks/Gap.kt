package com.eggnstone.jetbrainsplugins.dartformat.blocks

import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.IToken

class Gap(val tokens: List<IToken>) : IBlock
{
    override fun equals(other: Any?): Boolean = other is Gap && tokens == other.tokens

    override fun hashCode(): Int = tokens.hashCode()

    override fun toString(): String = "Gap(${Tools.toString(tokens)})"
}
