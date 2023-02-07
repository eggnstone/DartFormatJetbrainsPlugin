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

                var startPos = i
                var endPos1 = i
                var endPos2 = i

                while (startPos > 0 && actual[startPos] != '\n' && actual[startPos] != '\r')
                    startPos--
                while (startPos > 0 && (actual[startPos] == '\n' || actual[startPos] == '\r'))
                    startPos--
                while (startPos > 0 && actual[startPos] != '\n' && actual[startPos] != '\r')
                    startPos--

                while (endPos1 < actual.length && actual[endPos1] != '\n' && actual[endPos1] != '\r')
                    endPos1++
                while (endPos1 < actual.length && (actual[endPos1] == '\n' || actual[endPos1] == '\r'))
                    endPos1++
                while (endPos1 < actual.length && actual[endPos1] != '\n' && actual[endPos1] != '\r')
                    endPos1++

                while (endPos2 < expected.length && expected[endPos2] != '\n' && expected[endPos2] != '\r')
                    endPos2++
                while (endPos2 < expected.length && (expected[endPos2] == '\n' || expected[endPos2] == '\r'))
                    endPos2++
                while (endPos2 < expected.length && expected[endPos2] != '\n' && expected[endPos2] != '\r')
                    endPos2++

                val actualShort = actual.substring(startPos, endPos1)
                val expectedShort = expected.substring(startPos, endPos2)
                throw ShortAssertError(
                    "\nExpected: \"$expectedShort\"\n     but: was \"$actualShort\"",
                    "Difference at position $i."
                )
            }

            throw Exception("???")
        }
    }
}
