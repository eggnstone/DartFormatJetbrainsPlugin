package indenters.multiBlockIndenter.indentHeader

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyTextThrows()
    {
        val inputText = ""

        assertThrows<DartFormatException> { MultiBlockIndenter(4).indentHeader(inputText) }
    }

    @Test
    fun spaceOnlyThrows()
    {
        val inputText = " "

        assertThrows<DartFormatException> { MultiBlockIndenter(4).indentHeader(inputText) }
    }

    @Test
    fun spacesOnlyThrows()
    {
        val inputText = "  "

        assertThrows<DartFormatException> { MultiBlockIndenter(4).indentHeader(inputText) }
    }
}
