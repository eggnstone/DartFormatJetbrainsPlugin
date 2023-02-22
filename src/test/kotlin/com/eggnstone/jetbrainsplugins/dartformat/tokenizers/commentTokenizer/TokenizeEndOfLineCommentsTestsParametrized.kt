package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.commentTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.TokenizerException
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeEndOfLineCommentsTestsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun endOfLineCommentWithLineBreakMustThrowException()
    {
        val inputText = "//comment$newLine"

        assertThrows<TokenizerException> { CommentTokenizer().tokenize(inputText) }
    }
}
