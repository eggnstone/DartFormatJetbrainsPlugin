package indenters.singleBlock.indentHeader

import TestTools.Companion.assertAreEqual
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.SingleBlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.parts.Whitespace
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestSimple
{
    @Test
    fun oneLine()
    {
        val inputText = "class C {"

        val expectedText = "class C {"

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun twoLines()
    {
        val inputText = "class C\n{"

        val expectedText = "class C\n{"

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeLines()
    {
        val inputText = "class C\nwith X\n{"

        val expectedText = "class C\n    with X\n{"

        val actualText = SingleBlockIndenter().indentHeader(inputText)

        assertAreEqual(actualText, expectedText)
    }
}
