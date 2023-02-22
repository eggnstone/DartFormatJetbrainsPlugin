package com.eggnstone.jetbrainsplugins.dartformat.indenter

interface IIndent
{
    val level: Int

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}
