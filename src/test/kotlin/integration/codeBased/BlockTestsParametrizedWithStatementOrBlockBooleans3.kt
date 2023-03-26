package integration.codeBased

import TestParams
import integration.IntegrationTools
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class BlockTestsParametrizedWithStatementOrBlockBooleans3(
    useBlockText0: String,
    useBlockText1: String,
    useBlockText2: String,
    @Suppress("UNUSED_PARAMETER") unused: String
)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "{3}")
        fun data() = TestParams.statementOrBlockBooleans3
    }

    private val useBlock0: Boolean = useBlockText0 == "true"
    private val useBlock1: Boolean = useBlockText1 == "true"
    private val useBlock2: Boolean = useBlockText2 == "true"

    @Test
    fun ifElseIfElse()
    {
        val inputText =
            "if (a)\n" +
                (if (useBlock0) "{}" else "a();") + "\n" +
                "else if (b)\n" +
                (if (useBlock1) "{}" else "b();") + "\n" +
                "else\n" +
                (if (useBlock2) "{}" else "c();")

        val expectedOutputText =
            "if (a)\n" +
                (if (useBlock0) "{}" else "    a();") + "\n" +
                "else if (b)\n" +
                (if (useBlock1) "{}" else "    b();") + "\n" +
                "else\n" +
                (if (useBlock2) "{}" else "    c();")

        IntegrationTools.test(inputText, expectedOutputText, true)
    }
}
