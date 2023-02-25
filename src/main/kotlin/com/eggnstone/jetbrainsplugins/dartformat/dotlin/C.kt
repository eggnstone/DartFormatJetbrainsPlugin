package com.eggnstone.jetbrainsplugins.dartformat.dotlin

import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException

class C(val value: String)
{
    init
    {
        if (value.length != 1)
            throw DartFormatException("value.length != 1")
    }

    override fun equals(other: Any?): Boolean
    {
        if (other == null)
            throw DartFormatException("other == null")

        if (other::class.simpleName == "Char")
            throw DartFormatException("other::class.simpleName == \"Char\"")

        return other is C && value == other.value
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value
}
