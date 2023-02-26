package blockifiers.instruction

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.blockifiers.InstructionBlockifier
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class TestTrivial
{
    @Test
    fun empty()
    {
        val inputText = ""

        assertThrows<DartFormatException> { InstructionBlockifier().blockify(inputText) }
    }
}
