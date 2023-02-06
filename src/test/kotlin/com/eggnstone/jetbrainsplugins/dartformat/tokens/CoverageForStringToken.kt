package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForStringToken
{
    @Test
    fun testHashCode() = assertThat(StringToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(StringToken("a").toString(), equalTo("String(a)"))
}
