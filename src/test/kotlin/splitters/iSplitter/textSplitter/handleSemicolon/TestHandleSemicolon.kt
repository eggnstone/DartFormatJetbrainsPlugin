package splitters.iSplitter.textSplitter.handleSemicolon

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestHandleSemicolon
{
    @Test
    fun ifAndElsePart1()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1"
        inputState.remainingText = "; else statement2;"

        val expectedState = TextSplitterState("")
        expectedState.currentText ="if (true) statement1; else "
        expectedState.remainingText = "statement2;"

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)
        assertNull(actualHandleResult.splitResult)
    }

    @Test
    fun ifAndElsePart2()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1"
        inputState.remainingText = "; else statement2;"
        inputState.isDoubleBlock = true

        val expectedParts1 = listOf(Statement("statement1;"))
        val expectedParts2 = listOf(Statement("statement2;"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        //TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)
        assertNotNull(actualHandleResult.splitResult)

        val splitResult = actualHandleResult.splitResult!!
        MatcherAssert.assertThat(splitResult.remainingText, CoreMatchers.equalTo(""))
        MatcherAssert.assertThat(splitResult.parts, CoreMatchers.equalTo(expectedParts1))
    }
}
