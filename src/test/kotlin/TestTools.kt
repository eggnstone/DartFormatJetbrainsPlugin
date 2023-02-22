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

        fun assertAreEqual(actual: String, expected: String)
        {
            val maxCommonLength = actual.length.coerceAtMost(expected.length)
            if (actual.substring(0, maxCommonLength) == expected.substring(0, maxCommonLength))
            {
                if (actual.length > expected.length)
                    throw ShortAssertError("", "Actual is longer than expected.", 1)

                if (actual.length < expected.length)
                    throw ShortAssertError("", "Actual is shorter than expected.", 1)

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
