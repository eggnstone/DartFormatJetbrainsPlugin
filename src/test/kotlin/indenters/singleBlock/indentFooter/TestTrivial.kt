package indenters.singleBlock.indentFooter

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyText_throws()
    {
        val inputText = ""

        assertThrows<DartFormatException> { MultiBlockIndenter(4).indentFooter(inputText) }
    }
}
