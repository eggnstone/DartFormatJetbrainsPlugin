package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.TextToken2
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForTextToken
{
    @Test
    fun testHashCode() = assertThat(TextToken2("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(TextToken2("a").toString(), equalTo("Text(a)"))
}
