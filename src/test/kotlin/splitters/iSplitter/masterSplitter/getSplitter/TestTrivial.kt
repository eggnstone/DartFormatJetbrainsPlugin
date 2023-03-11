package splitters.iSplitter.masterSplitter.getSplitter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText()
    {
        val inputText = ""

        assertThrows<DartFormatException> { MasterSplitter().getSplitter(inputText) }
    }
}
