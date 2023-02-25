package com.eggnstone.jetbrainsplugins.dartformat.tokenizers.ClassKeywordTokenizer

import TestParams
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.ClassKeywordTokenizer
import com.eggnstone.jetbrainsplugins.dartformat.tokens.ClassKeywordToken
import com.eggnstone.jetbrainsplugins.dartformat.tokens.UnknownToken
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TokenizeTestsParametrized(private val classKeyword: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{0}")
        fun data() = TestParams.classKeywords
    }

    @Test
    fun classKeywordIgnoredWhenPartOfWord()
    {
        val inputText = "${classKeyword}C"
        val expectedTokens = mutableListOf(
            UnknownToken("${classKeyword}C")
        )

        val actualTokens = ClassKeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun classKeywordIgnoredWhenNotAtTextStart()
    {
        val inputText = "abc $classKeyword C"
        val expectedTokens = mutableListOf(
            UnknownToken("abc $classKeyword C")
        )

        val actualTokens = ClassKeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun classKeywordAlone()
    {
        val inputText = classKeyword
        val expectedTokens = mutableListOf(ClassKeywordToken(classKeyword))

        val actualTokens = ClassKeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }

    @Test
    fun classKeyword()
    {
        val inputText = "$classKeyword C"
        val expectedTokens = mutableListOf(
            ClassKeywordToken(classKeyword),
            UnknownToken(" C")
        )

        val actualTokens = ClassKeywordTokenizer().tokenize(inputText)

        assertThat(actualTokens, equalTo(expectedTokens))
    }
}
