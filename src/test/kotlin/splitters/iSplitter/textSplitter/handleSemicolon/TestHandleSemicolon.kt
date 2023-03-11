package splitters.iSplitter.textSplitter.handleSemicolon

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import junit.framework.TestCase.assertNull
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestHandleSemicolon
{
    @Test
    fun ifStatementAndElseStatementPart1()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1"
        inputState.remainingText = "; else statement2;"

        val expectedState = TextSplitterState("")
        expectedState.currentText = "if (true) statement1; else "
        expectedState.remainingText = "statement2;"

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)
        assertNull(actualHandleResult.splitResult)
    }

    @Test
    fun ifStatementAndElseStatementPart2()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1; else statement2"
        inputState.remainingText = ";"

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "if (true) statement1; else statement2;"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("if (true) statement1; else statement2;"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)

        val splitResult = actualHandleResult.splitResult!!
        TestTools.assertAreEqual("remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun ifBlockAndMissingElse()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1"
        inputState.remainingText = "; elseX"

        val expectedRemainingText = " elseX"
        val expectedState = TextSplitterState("")
        expectedState.currentText = "if (true) statement1;"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("if (true) statement1;"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)

        val splitResult = actualHandleResult.splitResult!!
        TestTools.assertAreEqual("remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }
}
