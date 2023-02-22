package com.eggnstone.jetbrainsplugins.dartformat.indenter

interface IIndent
{
    val level: Int

    override fun toString(): String
}
