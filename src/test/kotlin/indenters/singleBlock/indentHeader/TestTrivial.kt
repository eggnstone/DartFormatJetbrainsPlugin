package indenters.singleBlock.indentHeader

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.SingleBlockIndenter
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyTextThrows()
    {
        val inputText = ""

        assertThrows<DartFormatException> { SingleBlockIndenter(4).indentHeader(inputText) }
    }

    @Test
    fun spaceOnlyThrows()
    {
        val inputText = " "

        assertThrows<DartFormatException> { SingleBlockIndenter(4).indentHeader(inputText) }
    }
}
