package com.eggnstone.jetbrainsplugins.dartformat.tokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokens.MultiLineCommentToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CoverageForMultiLineCommentToken
{
    @Test
    fun testHashCode() = assertThat(MultiLineCommentToken("a").hashCode(), equalTo("a".hashCode()))

    @Test
    fun testToString() = assertThat(MultiLineCommentToken("a").toString(), equalTo("MultiLineComment(a)"))
}
