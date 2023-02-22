package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MultiLineCommentTokenCoverageTests
{
    @Test
    fun testHashCode() = assertThat(MultiLineCommentToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(MultiLineCommentToken("a").toString(), equalTo("MultiLineComment(\"a\")"))

    @Test
    fun testToStringNotClosed() = assertThat(MultiLineCommentToken("a", isClosed = false).toString(), equalTo("MultiLineComment(\"a\", isClosed=false)"))
}
