package indenters.singleBlock.indentHeader

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun emptyHeader()
    {
        val inputText = ""

        val expectedText = ""

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        assertAreEqual(actualText, expectedText)
    }
}
