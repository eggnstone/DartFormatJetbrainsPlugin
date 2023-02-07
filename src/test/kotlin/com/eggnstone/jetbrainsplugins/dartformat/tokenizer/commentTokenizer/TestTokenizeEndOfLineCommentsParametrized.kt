package com.eggnstone.jetbrainsplugins.dartformat.tokenizer.commentTokenizer

import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestTokenizeEndOfLineCommentsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") newLineName: String)
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

        assertThrows<Exception> { CommentTokenizer().tokenize(inputText) }
    }
}
