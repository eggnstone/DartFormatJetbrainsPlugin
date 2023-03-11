package splitters.textSplitter.handleSemicolon

import dev.eggnstone.plugins.jetbrains.dartformat.parts.DoubleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Statement
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import junit.framework.TestCase.assertNotNull
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestX
{
    @Test
    fun ifAndElse()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1"
        inputState.remainingText = "; else statement2;"

        val expectedRemainingText = ""
        val parts1 = listOf(Statement("statement1;"))
        val parts2 = listOf(Statement("statement2;"))
        val expectedPart = DoubleBlock("if (true) ", " else ", "", parts1, parts2)
        val expectedParts = listOf<IPart>(expectedPart)

        val actualHandleResult = TextSplitter.handleSemicolon(inputState)

        assertNotNull(actualHandleResult.splitResult)
        val actualState = actualHandleResult.state
        val actualSplitResult = actualHandleResult.splitResult!!

        MatcherAssert.assertThat(actualState.remainingText, CoreMatchers.equalTo(expectedRemainingText))

        MatcherAssert.assertThat(actualSplitResult.remainingText, CoreMatchers.equalTo(expectedRemainingText))
        MatcherAssert.assertThat(actualSplitResult.parts, CoreMatchers.equalTo(expectedParts))
    }
}
