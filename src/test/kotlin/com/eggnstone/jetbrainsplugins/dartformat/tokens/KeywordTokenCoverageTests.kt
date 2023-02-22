package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class KeywordTokenCoverageTests
{
    @Test
    fun testHashCode() = assertThat(KeywordToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(KeywordToken("a").toString(), equalTo("Keyword(\"a\")"))
}
