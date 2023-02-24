package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class BlockifyTests
{
    @Test
    fun emptyText()
    {
        val inputText = ""
        val expectedBlocks = arrayListOf<IBlock>()

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun oneSimpleExpression()
    {
        val inputText = "abc();"

        val expressionBlock = ExpressionBlock("abc();")
        val expectedBlocks = arrayListOf(expressionBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun twoSimpleExpressions()
    {
        val inputText = "abc();\ndef();"

        val block1 = ExpressionBlock("abc();")
        val block2 = WhitespaceBlock("\n")
        val block3 = ExpressionBlock("def();")
        val expectedBlocks = arrayListOf(block1, block2, block3)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun expressionWithSingleQuoteString()
    {
        val inputText = "import 'package:dart_format/src/blockifier/Blockifier.dart';"

        val expressionBlock = ExpressionBlock(inputText)
        val expectedBlocks = arrayListOf(expressionBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun simpleCurlyBracketsBlock()
    {
        val inputText = "{}"

        val curlyBracketsBlock = CurlyBracketsBlock("")
        val expectedBlocks = arrayListOf(curlyBracketsBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun simpleCurlyBracketsBlockWithLineBreakAtTextEnd()
    {
        val inputText = "{}\n"

        val curlyBracketsBlock = CurlyBracketsBlock("")
        val whitespaceBlock = WhitespaceBlock("\n")
        val expectedBlocks = arrayListOf(curlyBracketsBlock, whitespaceBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun simpleClass()
    {
        val inputText = "class C\n{\n}"

        val innerBlock = WhitespaceBlock("\n")
        val innerBlocks = arrayListOf(innerBlock)
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = arrayListOf(classBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun simpleAbstractClass()
    {
        val inputText = "abstract class C\n{\n}"

        val innerBlock = WhitespaceBlock("\n")
        val innerBlocks = arrayListOf(innerBlock)
        val classBlock = ClassBlock("abstract class C\n", innerBlocks)
        val expectedBlocks = arrayListOf(classBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }

    @Test
    fun simpleClassWithExpression()
    {
        val inputText = "class C\n{\nabc();\n}"

        val innerBlock1 = WhitespaceBlock("\n")
        val innerBlock2 = ExpressionBlock("abc();")
        val innerBlock3 = WhitespaceBlock("\n")
        val innerBlocks = arrayListOf(innerBlock1, innerBlock2, innerBlock3)
        val classBlock = ClassBlock("class C\n", innerBlocks)
        val expectedBlocks = arrayListOf(classBlock)

        val blockifier = Blockifier()
        val actualBlocks = blockifier.blockify(inputText)

        MatcherAssert.assertThat(actualBlocks, equalTo(expectedBlocks))
    }
}
