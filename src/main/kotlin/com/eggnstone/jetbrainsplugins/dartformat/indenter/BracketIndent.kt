package com.eggnstone.jetbrainsplugins.dartformat.indenter

class BracketIndent(val text: String, override val level: Int) : IIndent
{
    companion object
    {
        /*val NONE = BracketIndent("")

        val ANGLE = BracketIndent("<")
        val CURLY = BracketIndent("{")
        val ROUND = BracketIndent("(")
        val SQUARE = BracketIndent("[")*/
    }

    override fun equals(other: Any?): Boolean //= other is BracketIndent && text == other.text
    {
        TODO()
    }

    override fun hashCode(): Int
    {
        TODO()
    }

    override fun toString(): String = "$text/$level"
}
