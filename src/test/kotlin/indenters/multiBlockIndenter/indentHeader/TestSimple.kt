package indenters.multiBlockIndenter.indentHeader

import TestTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MultiBlockIndenter
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Ignore
import org.junit.Test

class TestSimple
{
    @Test
    fun simple()
    {
        val inputText = "header{"

        val expectedText = "header{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun trimStart()
    {
        val inputText = "@annotation\n    header{"

        val expectedText = "@annotation\nheader{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    @Ignore
    fun trimEnd()
    {
        val inputText = "@annotation\nheader    \n{"

        val expectedText = "@annotation\nheader\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun oneLine()
    {
        val inputText = "class C {"

        val expectedText = "class C {"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun endOfLineCommentWithQuotesAtTextStart()
    {
        val inputText =
            "//\"comment1\"\n" +
            "//    \"comment2\"\n" +
            "void main()\n" +
            "{"

        val expectedText =
            "//\"comment1\"\n" +
            "//    \"comment2\"\n" +
            "void main()\n" +
            "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    @Ignore
    fun endOfLineCommentWithQuotesAtTextStartAndTrailingSpaces_trimEnd()
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
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
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun constructorWithAssignment()
    {
        val inputText =
            "C()\n" +
            ": a = b\n" +
            "{"

        val expectedText =
            "C()\n" +
            "    : a = b\n" +
            "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun constructorWithAssignmentSplit()
    {
        val inputText =
            "C()\n" +
            ":\n" +
            "a = b\n" +
            "{"

        val expectedText =
            "C()\n" +
            "    :\n" +
            "      a = b\n" +
            "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun constructorWithAssignments()
    {
        val inputText =
            "C()\n" +
            ": a = b,\n" +
            "c = d\n" +
            "{"

        val expectedText =
            "C()\n" +
            "    : a = b,\n" +
            "      c = d\n" +
            "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun constructorWithAssignmentsSplit()
    {
        val inputText =
            "C()\n" +
            ":\n" +
            "a = b,\n" +
            "c = d\n" +
            "{"

        val expectedText =
            "C()\n" +
            "    :\n" +
            "      a = b,\n" +
            "      c = d\n" +
            "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun emptyHeader()
    {
        val inputText = "{"

        val expectedText = "{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun headerWithOneLineBreak()
    {
        val inputText = "\n{"

        val expectedText = "\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }

    @Test
    fun headerWithTwoLineBreaks()
    {
        val inputText = "\n\n{"

        val expectedText = "\n\n{"
        val expectedLevel = 0

        val actualResult = MultiBlockIndenter(4).indentHeader(inputText)

        TestTools.assertAreEqual("Text", actualResult.text, expectedText)
        MatcherAssert.assertThat("Level", actualResult.level, CoreMatchers.equalTo(expectedLevel))
    }
}
