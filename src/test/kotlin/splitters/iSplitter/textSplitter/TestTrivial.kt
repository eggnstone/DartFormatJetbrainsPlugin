package splitters.iSplitter.textSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.TextSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        assertThrows<DartFormatException> { TextSplitter().split(inputText) }
    }
}
