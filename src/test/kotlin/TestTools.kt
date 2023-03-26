import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools
import org.hamcrest.*

class TestTools
{
    companion object
    {
        fun <T> assertThat(actual: T, matcher: Matcher<in T>) = assertThat("", actual, matcher, 2)

        fun <T> assertThat(reason: String, actual: T, matcher: Matcher<in T>, stackPos: Int = 2)
        {
            if (matcher.matches(actual))
                return

            val description: Description = StringDescription()
            description.appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\n     but: ")
            matcher.describeMismatch(actual, description)
            throw ShortAssertError(description.toString(), if (reason.isEmpty()) "" else "Reason: $reason", stackPos)
        }

        fun assertStringsAreEqual(actual: List<String>, expected: List<String>) = assertStringsAreEqual("", actual, expected)

        fun assertStringsAreEqual(reason: String, actual: List<String>, expected: List<String>)
        {
            assertAreEqualInternal(reason, Tools.toDisplayStringForStrings(actual), Tools.toDisplayStringForStrings(expected), 3)
            //MatcherAssert.assertThat(actual, CoreMatchers.equalTo(expected))
        }

        fun assertPartsAreEqual(actual: List<IPart>, expected: List<IPart>) = assertPartsAreEqual("", actual, expected)

        fun assertPartsAreEqual(reason: String, actual: List<IPart>, expected: List<IPart>)
        {
            assertAreEqualInternal(reason, Tools.toDisplayStringForParts(actual), Tools.toDisplayStringForParts(expected), 3)
            //MatcherAssert.assertThat(actual, CoreMatchers.equalTo(expected))
        }

        fun assertAreEqual(reason: String, actual: String, expected: String, stackPos: Int = 3)
        {
            val actualSimple = Tools.toDisplayStringSimple(actual)
            val expectedSimple = Tools.toDisplayStringSimple(expected)

            assertAreEqualInternal(reason, actualSimple, expectedSimple, stackPos)
        }

        private fun assertAreEqualInternal(reason: String?, actual: String, expected: String, stackPos: Int)
        {
            if (reason == null)
                DotlinLogger.log("REASON IS MISSING!")

            val reasonText = if (reason == null || StringWrapper.isEmpty(reason)) "" else " ($reason)"

            val maxCommonLength = actual.length.coerceAtMost(expected.length)
            if (actual.substring(0, maxCommonLength) == expected.substring(0, maxCommonLength))
            {
                if (actual.length > expected.length)
                    throw ShortAssertError(
                        "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                        "Actual is longer than expected.$reasonText",
                        stackPos
                    )

                if (actual.length < expected.length)
                    throw ShortAssertError(
                        "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                        "Actual is shorter than expected.$reasonText",
                        stackPos
                    )

                return
            }

            for (i in 0..maxCommonLength)
            {
                if (actual[i] == expected[i])
                    continue

                throw ShortAssertError(
                    "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                    "Difference at position $i.$reasonText",
                    stackPos
                )
            }

            throw Exception("???")
        }

        fun assertStatesAreEqual(actualState: TextSplitterState, expectedState: TextSplitterState)
        {
            assertAreEqual("currentText", actualState.currentText, expectedState.currentText)
            assertAreEqual("remainingText", actualState.remainingText, expectedState.remainingText)
            MatcherAssert.assertThat("currentBrackets", actualState.currentBrackets, CoreMatchers.equalTo(expectedState.currentBrackets))
            MatcherAssert.assertThat("hasBlock", actualState.hasBlock, CoreMatchers.equalTo(expectedState.hasBlock))
            //MatcherAssert.assertThat("isFirstBlockWithBrackets", actualState.isFirstBlockWithBrackets, CoreMatchers.equalTo(expectedState.isFirstBlockWithBrackets))
            //MatcherAssert.assertThat("isSecondBlockWithBrackets", actualState.isSecondBlockWithBrackets, CoreMatchers.equalTo(expectedState.isSecondBlockWithBrackets))
            assertAreEqual("header", actualState.header, expectedState.header)
            assertAreEqual("middle", actualState.middle, expectedState.middle)
            assertAreEqual("footer", actualState.footer, expectedState.footer)
            MatcherAssert.assertThat("parts1", actualState.blockParts, CoreMatchers.equalTo(expectedState.blockParts))
        }
    }
}
