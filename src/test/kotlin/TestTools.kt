import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.StringDescription

class TestTools
{
    companion object
    {
        fun <T> assertThat(actual: T, matcher: Matcher<in T>)
        {
            if (matcher.matches(actual))
                return

            val description: Description = StringDescription()
            description.appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\n     but: ")
            matcher.describeMismatch(actual, description)
            throw ShortAssertError(description.toString(), "")
        }

        fun assertAreEqual(actual: String, expected: String)
        {
            val maxCommonLength = actual.length.coerceAtMost(expected.length)
            if (actual.substring(0, maxCommonLength) == expected.substring(0, maxCommonLength))
            {
                if (actual.length > expected.length)
                    throw ShortAssertError("", "Actual is longer than expected.")

                if (actual.length < expected.length)
                    throw ShortAssertError("", "Actual is shorter than expected.")

                return
            }

            for (i in 0..maxCommonLength)
            {
                if (actual[i] == expected[i])
                    continue

                throw ShortAssertError(
                    "\nExpected: \"$expected\"\n     but: was \"$actual\"",
                    "Difference at position $i."
                )
            }

            throw Exception("???")
        }
    }
}
