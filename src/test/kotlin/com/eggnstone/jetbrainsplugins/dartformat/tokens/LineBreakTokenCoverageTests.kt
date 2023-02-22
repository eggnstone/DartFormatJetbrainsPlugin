package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class LineBreakTokenCoverageTests
{
    @Test
    fun testHashCode() = assertThat(LineBreakToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(LineBreakToken("a").toString(), equalTo("LineBreak(\"a\")"))
}
