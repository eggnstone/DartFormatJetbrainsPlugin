import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription

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

        fun assertStringsAreEqual(actual: List<String>, expected: List<String>)
        {
            assertAreEqualInternal(Tools.toDisplayStringForStrings(actual), Tools.toDisplayStringForStrings(expected), 3)
            //MatcherAssert.assertThat(actual, CoreMatchers.equalTo(expected))
        }

        fun assertPartsAreEqual(actual: List<IPart>, expected: List<IPart>)
        {
            assertAreEqualInternal(Tools.toDisplayStringForParts(actual), Tools.toDisplayStringForParts(expected), 3)
            //MatcherAssert.assertThat(actual, CoreMatchers.equalTo(expected))
        }

        fun assertAreEqual(actual: String, expected: String)
        {
            val actualSimple = Tools.toDisplayStringSimple(actual)
            val expectedSimple = Tools.toDisplayStringSimple(expected)

            assertAreEqualInternal(actualSimple, expectedSimple, 3)
        }

        private fun assertAreEqualInternal(actual: String, expected: String, stackPos: Int)
        {
            val maxCommonLength = actual.length.coerceAtMost(expected.length)
            if (actual.substring(0, maxCommonLength) == expected.substring(0, maxCommonLength))
            {
                if (actual.length > expected.length)
                    throw ShortAssertError(
                        "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                        "Actual is longer than expected.",
                        stackPos
                    )

                if (actual.length < expected.length)
                    throw ShortAssertError(
                        "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                        "Actual is shorter than expected.",
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
                    "Difference at position $i.",
                    stackPos
                )
            }

            throw Exception("???")
        }
    }
}
