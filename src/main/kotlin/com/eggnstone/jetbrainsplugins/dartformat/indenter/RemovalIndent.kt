package com.eggnstone.jetbrainsplugins.dartformat.indenter

class RemovalIndent(override val level: Int) : IIndent
{
    override fun toString(): String = "Removal($level)"
}
