package com.eggnstone.jetbrainsplugins.dartformat.tokens

interface IToken
{
    fun recreate(): String

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}
