package indenters.singleBlock.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.SingleBlockIndenter
import org.junit.Test

class TestSimple
{
    @Test
    fun oneLine()
    {
        val inputText = "class C {"

        val expectedText = "class C {"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun twoLines()
    {
        val inputText =
            "class C\n" +
                "{"

        val expectedText =
            "class C\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun threeLines()
    {
        val inputText =
            "class C\n" +
                "with X\n" +
                "{"

        val expectedText =
            "class C\n" +
                "    with X\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun voidMainAsync()
    {
        val inputText =
            "void main()\n" +
                "async\n" +
                "{"

        val expectedText =
            "void main()\n" +
                "async\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun annotation()
    {
        val inputText =
            "@annotation\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "@annotation\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun annotations()
    {
        val inputText =
            "@annotation1\n" +
                "@annotation2\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "@annotation1\n" +
                "@annotation2\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun endOfLineCommentAtTextStart()
    {
        val inputText =
            "//comment\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "//comment\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun endOfLineCommentWithQuotesAtTextStart()
    {
        val inputText =
            "//\"comment1\"\n" +
                "//    \"comment2\"    \n" +
                "void main()\n" +
                "{"

        val expectedText =
            "//\"comment1\"\n" +
                "//    \"comment2\"\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun endOfLineCommentAtTextMiddle()
    {
        val inputText =
            "void main()\n" +
                "//comment\n" +
                "{"

        val expectedText =
            "void main()\n" +
                "//comment\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextStart()
    {
        val inputText =
            "/*comment*/\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "/*comment*/\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextStartWithoutLineBreak()
    {
        val inputText =
            "/*comment*/void main()\n" +
                "{"

        val expectedText =
            "/*comment*/void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextStart2()
    {
        val inputText =
            "/*comment1\n" +
                "comment2*/\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "/*comment1\n" +
                "comment2*/\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextStart3()
    {
        val inputText =
            "/*comment1\n" +
                "comment2\n" +
                "comment3*/\n" +
                "void main()\n" +
                "{"

        val expectedText =
            "/*comment1\n" +
                "comment2\n" +
                "comment3*/\n" +
                "void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextStart2WithoutLineBreak()
    {
        val inputText =
            "/*comment1\n" +
                "comment2*/void main()\n" +
                "{"

        val expectedText =
            "/*comment1\n" +
                "comment2*/void main()\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextMiddle()
    {
        val inputText =
            "void main()\n" +
                "/*comment*/\n" +
                "{"

        val expectedText =
            "void main()\n" +
                "/*comment*/\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextMiddle2()
    {
        val inputText =
            "void main()\n" +
                "/*comment1\n" +
                "comment2*/\n" +
                "{"

        val expectedText =
            "void main()\n" +
                "/*comment1\n" +
                "comment2*/\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun multiCommentAtTextMiddle3()
    {
        val inputText =
            "void main()\n" +
                "/*comment1\n" +
                "comment2\n" +
                "comment3*/\n" +
                "{"

        val expectedText =
            "void main()\n" +
                "/*comment1\n" +
                "comment2\n" +
                "comment3*/\n" +
                "{"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }

    @Test
    fun constructorWithAssignments()
    {
        val inputText =
            "C()\n" +
                ": a = b,\n" +
                "a = b;"

        val expectedText =
            "C()\n" +
                "    : a = b,\n" +
                "    a = b;"

        val actualText = SingleBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual(actualText, expectedText)
    }
}
