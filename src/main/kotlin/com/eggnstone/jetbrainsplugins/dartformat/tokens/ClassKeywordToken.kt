package com.eggnstone.jetbrainsplugins.dartformat.tokens

class ClassKeywordToken(val text: String) : IToken
{
    val isMainClassKeyword: Boolean = text == "class" || text == "abstract class"

    override fun equals(other: Any?): Boolean = other is ClassKeywordToken && text == other.text

    override fun hashCode(): Int = text.hashCode()

    override fun recreate(): String = text

    override fun toString(): String = "ClassKeyword(\"$text\")"
}
