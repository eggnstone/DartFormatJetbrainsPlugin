package integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.PartTools
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.MasterSplitter

class IntegrationTools
{
    companion object
    {
        fun test(inputText: String, expectedOutputText: String)
        {
            val inputTokens = Tokenizer().tokenize(inputText)
            val actualOutputTokens = Formatter().format(inputTokens)
            //val actualOutputText = Indenter().indent(actualOutputTokens)
            val tempText = Indenter().recreate(actualOutputTokens)

            val parts = MasterSplitter().splitAll(tempText)
            PartTools.printParts(parts)

            val actualOutputText = MasterIndenter().indentParts(parts)
            val actualOutputText2= Tools.toDisplayStringSimple( actualOutputText)
            val expectedOutputText2= Tools.toDisplayStringSimple( expectedOutputText)

            //TestTools.assertAreEqual(actualOutputText, expectedOutputText)
            TestTools.assertAreEqual(actualOutputText2, expectedOutputText2)
        }
    }
}
