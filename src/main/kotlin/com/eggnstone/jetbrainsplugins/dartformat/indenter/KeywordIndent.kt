package com.eggnstone.jetbrainsplugins.dartformat.indenter

class KeywordIndent(val text: String, override val level: Int) : IIndent
{
    override fun equals(other: Any?): Boolean //= other is KeywordIndent && text == other.text
    {
        TODO()
    }

    override fun hashCode(): Int
    {
        TODO()
    }

    override fun toString(): String = "$text/$level"
}
