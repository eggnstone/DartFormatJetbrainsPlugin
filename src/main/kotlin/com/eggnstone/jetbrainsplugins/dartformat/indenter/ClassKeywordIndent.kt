package com.eggnstone.jetbrainsplugins.dartformat.indenter

class ClassKeywordIndent(val text: String, override val level: Int) : IIndent
{
    override fun toString(): String = "$text/$level"
}
