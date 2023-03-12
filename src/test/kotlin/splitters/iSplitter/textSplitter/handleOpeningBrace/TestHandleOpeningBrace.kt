package splitters.iSplitter.textSplitter.handleOpeningBrace

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitterState
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestHandleOpeningBrace
{
    @Test
    fun missingClosingBrace_throws()
    {
        val inputState = TextSplitterState("")
        inputState.currentText = "if (true) statement1; "
        inputState.remainingText = "{ statement2;"

        assertThrows<DartFormatException> { TextSplitter.handleOpeningBrace(inputState) }
    }
}
