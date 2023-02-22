package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StringTokenCoverageTests
{
    @Test
    fun testHashCode() = assertThat(StringToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testRecreate() = assertThat(StringToken("a").recreate(), equalTo("a"))

    @Test
    fun testToString() = assertThat(StringToken("a").toString(), equalTo("String(\"a\")"))

    @Test
    fun testToStringNotClosed() = assertThat(StringToken("a", isClosed = false).toString(), equalTo("String(\"a\", isClosed=false)"))
}
