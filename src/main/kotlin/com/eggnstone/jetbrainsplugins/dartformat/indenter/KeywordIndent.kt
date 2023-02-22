package com.eggnstone.jetbrainsplugins.dartformat.indenter

class KeywordIndent(val text: String, override val level: Int) : IIndent
{
    override fun toString(): String = "$text/$level"
}
