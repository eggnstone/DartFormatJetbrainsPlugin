package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.WhiteSpaceToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForWhiteSpaceToken
{
    @Test
    fun testHashCode() = assertThat(WhiteSpaceToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(WhiteSpaceToken("a").toString(), equalTo("WhiteSpace(a)"))
}
