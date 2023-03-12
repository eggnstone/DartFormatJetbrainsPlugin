/*
package splitters.iSplitter.textSplitter.handleSemicolon

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(value = Parameterized::class)
class TestHandleSemicolonParametrizedWithBool(private val b: Boolean)
{
    companion object
    {
        @JvmStatic
        @Parameterized.Parameters(name = "hasBlock: {0}")
        fun data() = listOf(false, true)
    }

    @Test
    fun simpleStatement()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = ";"
        inputState.hasBlock = b

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc();"
        expectedState.remainingText = expectedRemainingText
        expectedState.hasBlock = b

        val expectedParts = listOf(Statement("abc();"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)

        val splitResult = actualHandleResult.splitResult!!
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineComment()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment"
        inputState.hasBlock = b

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment"
        expectedState.remainingText = expectedRemainingText
        expectedState.hasBlock = b

        val expectedParts = listOf(Statement("abc(); // end of line comment"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)

        val splitResult = actualHandleResult.splitResult!!
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineCommentAndLineBreak()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment\n"
        inputState.hasBlock = b

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment\n"
        expectedState.remainingText = expectedRemainingText
        expectedState.hasBlock = b

        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)

        val splitResult = actualHandleResult.splitResult!!
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }
}
*/
