package com.eggnstone.jetbrainsplugins.dartformat.integration

import TestTools
import com.eggnstone.jetbrainsplugins.dartformat.formatters.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.indenter.Indenter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizers.Tokenizer
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

            TestTools.assertAreEqual(actualOutputText, expectedOutputText)
        }
    }
}
