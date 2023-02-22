package com.eggnstone.jetbrainsplugins.dartformat.indenter

class BracketIndent(val text: String, override val level: Int) : IIndent
{
    override fun toString(): String = "$text/$level"
}
