package blockifiers.instruction

import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blocks.InstructionBlock
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test

class TestCurlyBracketBlocks
{
    @Test
    fun simpleBlock()
    {
        val inputText = "{}"

        val expectedRemainingText = ""
        val expectedBlock = InstructionBlock("{", "}")

        val result = InstructionBlockifier().blockify(inputText)

        MatcherAssert.assertThat(result.remainingText, equalTo(expectedRemainingText))
        MatcherAssert.assertThat(result.block, equalTo(expectedBlock))

        DotlinLogger.log(result.block.toString())
    }
}
