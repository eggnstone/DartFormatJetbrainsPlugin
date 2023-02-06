package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForUnknownToken
{
    @Test
    fun testEquals() = assertThat(UnknownToken("a") == UnknownToken("a"), equalTo(true))

    @Test
    fun testHashCode() = assertThat(UnknownToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testRecreate() = assertThat(UnknownToken("a").recreate(), equalTo("a"))

    @Test
    fun testToString() = assertThat(UnknownToken("a").toString(), equalTo("Unknown(a)"))
}
