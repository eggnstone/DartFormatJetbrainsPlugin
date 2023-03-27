package splitters.iSplitter.textSplitter.handleSemicolon

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterHandleSplitResult
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterHandleStateResult
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
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

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleStateResult

        TestTools.assertStatesAreEqual(actualHandleResult.state, expectedState)
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

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
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

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatement()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = ";"

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc();"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc();"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineComment()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment"

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc(); // end of line comment"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineCommentAndLineBreak()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment\n"

        val expectedRemainingText = ""
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment\n"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineCommentAndLineBreakTODO1()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment\n}"

        val expectedRemainingText = "}"
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment\n"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineCommentAndLineBreakTODO2()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment\n}a"

        val expectedRemainingText = "}a"
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment\n"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }

    @Test
    fun simpleStatementWithEndOfLineCommentAndLineBreakTODO3()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "abc()"
        inputState.remainingText = "; // end of line comment\n}\n"

        val expectedRemainingText = "}\n"
        val expectedState = TextSplitterState("")
        expectedState.currentText = "abc(); // end of line comment\n"
        expectedState.remainingText = expectedRemainingText

        val expectedParts = listOf(Statement("abc(); // end of line comment\n"))

        val actualHandleResult = TextSplitter.handleSemicolon(inputState) as TextSplitterHandleSplitResult

        val splitResult = actualHandleResult.splitResult
        TestTools.assertAreEqual("splitResult.remainingText", splitResult.remainingText, expectedRemainingText)
        MatcherAssert.assertThat("splitResult.parts", splitResult.parts, CoreMatchers.equalTo(expectedParts))
    }
}
