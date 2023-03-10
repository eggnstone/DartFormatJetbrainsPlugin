package com.eggnstone.jetbrainsplugins.dartformat.tokens

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class EndOfLineCommentTokenCoverageTests
{
    @Test
    fun testHashCode() = assertThat(EndOfLineCommentToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(EndOfLineCommentToken("a").toString(), equalTo("EndOfLineComment(\"a\")"))
}
