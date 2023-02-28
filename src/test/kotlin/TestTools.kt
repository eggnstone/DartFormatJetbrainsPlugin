import dev.eggnstone.plugins.jetbrains.dartformat.Tools
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

        fun assertAreEqual(actual: List<String>, expected: List<String>)
        {
            assertAreEqual(Tools.toDisplayStringForStrings(actual), Tools.toDisplayStringForStrings(expected), 2) // stackPos doesn't seem to work
            //MatcherAssert.assertThat(actual, CoreMatchers.equalTo(expected))
        }

        fun assertAreEqual(actual: String, expected: String, stackPos: Int = 1)
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
                    1
                )
            }

            throw Exception("???")
        }
    }
}
