package blockifiers.master.getBlockifier

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.MasterBlockifier
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.WhitespaceBlockifier
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun empty()
    {
        val inputText = ""

        assertThrows<DartFormatException> { MasterBlockifier().getBlockifier(inputText) }
    }

    @Test
    fun whitespace()
    {
        val inputText = " "

        val blockifier = MasterBlockifier().getBlockifier(inputText)

        MatcherAssert.assertThat(blockifier is WhitespaceBlockifier, equalTo(true))
    }

    @Test
    fun nonWhitespace()
    {
        val inputText = ";"

        val blockifier = MasterBlockifier().getBlockifier(inputText)

        MatcherAssert.assertThat(blockifier is InstructionBlockifier, equalTo(true))
    }
}
