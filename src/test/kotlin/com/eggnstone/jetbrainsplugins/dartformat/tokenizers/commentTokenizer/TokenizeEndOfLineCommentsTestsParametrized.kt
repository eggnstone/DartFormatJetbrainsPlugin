package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.commentTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.CommentTokenizer
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeEndOfLineCommentsTestsParametrized(private val newLine: String, @Suppress("UNUSED_PARAMETER") unused: String)
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

        assertThrows<DartFormatException> { CommentTokenizer().tokenize(inputText) }
    }
}
