package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Ignore
import org.junit.Test

class BlockifyTests
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val expectedBlocks = mutableListOf<IBlock>()

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun oneSimpleExpression()
    {
        val inputText = "abc();"

        val expressionBlock = ExpressionBlock("abc();")
        val expectedBlocks = mutableListOf(expressionBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun twoSimpleExpressions()
    {
        val inputText = "abc();\ndef();"

        val block1 = ExpressionBlock("abc();")
        val block2 = WhitespaceBlock("\n")
        val block3 = ExpressionBlock("def();")
        val expectedBlocks = mutableListOf(block1, block2, block3)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun expressionWithSingleQuoteString()
    {
        val inputText = "import 'package:dart_format/src/blockifier/Blockifier.dart';"

        val expressionBlock = ExpressionBlock(inputText)
        val expectedBlocks = mutableListOf(expressionBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleCurlyBracketBlock()
    {
        val inputText = "{}"

        val curlyBracketBlock = CurlyBracketBlock(mutableListOf(UnknownBlock("")))
        val expectedBlocks = mutableListOf(curlyBracketBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleCurlyBracketBlockWithLineBreakAtTextEnd()
    {
        val inputText = "{}\n"

        val curlyBracketBlock = CurlyBracketBlock(mutableListOf(UnknownBlock("")))
        val whitespaceBlock = WhitespaceBlock("\n")
        val expectedBlocks = mutableListOf(curlyBracketBlock, whitespaceBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    /*@Test
    fun twoNestedCurlyBracketBlocks()
    {
        val inputText = "{{}}"

        val innerBlock = CurlyBracketBlock("")
        val outerBlock = CurlyBracketBlock(arrayOf(innerBlock))
        val expectedBlocks = arrayOf(outerBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }*/

    @Test
    fun simpleClass()
    {
        val inputText = "class C\n{}"

        val innerBlocks = mutableListOf<IBlock>()
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleClassWithLineBreak()
    {
        val inputText = "class C\n{\n}"

        val innerBlock = WhitespaceBlock("\n")
        val innerBlocks = mutableListOf(innerBlock)
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    @Ignore
    fun simpleClassWithCurlyBracketBlock()
    {
        val inputText = "class C\n{{}}"

        val innerBlock = CurlyBracketBlock(mutableListOf(UnknownBlock("")))
        val innerBlocks = mutableListOf(innerBlock)
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C\n{}"

        val innerBlocks = mutableListOf<IBlock>()
        val classBlock = ClassBlock("abstract class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleAbstractClassWithLineBreak()
    {
        val inputText = "abstract class C\n{\n}"

        val innerBlock = WhitespaceBlock("\n")
        val innerBlocks = mutableListOf(innerBlock)
        val classBlock = ClassBlock("abstract class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }

    @Test
    fun simpleClassWithExpression()
    {
        val inputText = "class C\n{\nabc();\n}"

        val innerBlock1 = WhitespaceBlock("\n")
        val innerBlock2 = ExpressionBlock("abc();")
        val innerBlock3 = WhitespaceBlock("\n")
        val innerBlocks = mutableListOf(innerBlock1, innerBlock2, innerBlock3)
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = mutableListOf(classBlock)

        val blockifier = BlockifierOld()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))

        blockifier.printBlocks(actualBlocks)
    }
}
