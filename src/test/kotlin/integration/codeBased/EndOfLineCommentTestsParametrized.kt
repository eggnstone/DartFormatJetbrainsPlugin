package integration.codeBased

import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class EndOfLineCommentTestsParametrized(private val lineBreak: String, @Suppress("UNUSED_PARAMETER") unused: String)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}")
        fun data() = TestParams.lineBreaks
    }

    @Test
    fun testEndOfLineCommentAtTextMiddle()
    {
        val inputText = "abc;//this is an end of line comment${lineBreak}def;"

        IntegrationTools.test(inputText, inputText)
    }
}
