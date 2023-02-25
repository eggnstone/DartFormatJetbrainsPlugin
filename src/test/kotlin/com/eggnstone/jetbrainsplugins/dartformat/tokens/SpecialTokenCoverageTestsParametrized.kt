package com.eggnstone.jetbrainsplugins.dartformat.tokens

import TestParams
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class SpecialTokenCoverageTestsParametrized(private val openingBracket: String, private val closingBracket: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{2}")
        fun data() = TestParams.brackets
    }

    @Test
    fun testIsOpeningBracket() = assertThat(SpecialToken(openingBracket).isOpeningBracket, equalTo(true))

    @Test
    fun testIsClosingBracket() = assertThat(SpecialToken(closingBracket).isClosingBracket, equalTo(true))
}
