package com.eggnstone.jetbrainsplugins.dartformat.indenter

import com.eggnstone.jetbrainsplugins.dartformat.Constants
import com.eggnstone.jetbrainsplugins.dartformat.Tools
import com.eggnstone.jetbrainsplugins.dartformat.tokens.*
import java.util.*

class Indenter(private val spacesPerLevel: Int = 4)
{
    fun indent(inputTokens: List<IToken>): String
    {
        val sb = StringBuilder()

        var tokens = inputTokens

        while (tokens.isNotEmpty())
        {
            val result = indentTokens(tokens)
            for (line in result.lines)
                sb.append(line)

            tokens = result.remainingTokens
        }

        return sb.toString()
    }

    private fun indentTokens(inputTokens: List<IToken>): IndentResult
    {
        println("indentTokens: ${Tools.toString(inputTokens)}")

        val lines = arrayListOf<String>()
        val remainingTokens = inputTokens.toMutableList()

        var currentLevel = 0
        var currentLine = ""
        val currentStack = Stack<IIndent>()
        val newStack = Stack<IIndent>()

        for (token in inputTokens)
        {
            //println("  token: $token")
            remainingTokens.removeAt(0)

            val wasCurrentLineEmpty = currentLine.isEmpty()

            // Remove leading white space
            if (token is WhiteSpaceToken)
            {
                if (wasCurrentLineEmpty)
                {
                    println("  Token is white space & line is empty => ignore")
                    println("    Current stack: \"" + Tools.toString(currentStack) + "\"")
                    println("    New stack:     \"" + Tools.toString(newStack) + "\"")
                    println("    Current line:  \"" + Tools.toDisplayString(currentLine) + "\"")
                    continue
                }
            }

            currentLine += token.recreate()

            if (token is KeywordToken)
            {
                if (wasCurrentLineEmpty)
                {
                    println("  Token is keyword => push keyword to new stack")
                    newStack.push(KeywordIndent(token.text, -1))
                }

                println("    Current stack: \"" + Tools.toString(currentStack) + "\"")
                println("    New stack:     \"" + Tools.toString(newStack) + "\"")
                println("    Current line:  \"" + Tools.toDisplayString(currentLine) + "\"")
                continue
            }

            if (token is SpecialToken)
            {
                //var bracketType : BracketIndent? = null
                var openingBracket = ""
                var closingBracket = ""
                when (token.text)
                {
                    Constants.OPENING_ANGLE_BRACKET, Constants.CLOSING_ANGLE_BRACKET ->
                    {
                        //bracketType = BracketIndent.ANGLE
                        openingBracket = Constants.OPENING_ANGLE_BRACKET
                        closingBracket = Constants.CLOSING_ANGLE_BRACKET
                    }

                    Constants.OPENING_CURLY_BRACKET, Constants.CLOSING_CURLY_BRACKET ->
                    {
                        //bracketType = BracketIndent.CURLY
                        openingBracket = Constants.OPENING_CURLY_BRACKET
                        closingBracket = Constants.CLOSING_CURLY_BRACKET
                    }

                    Constants.OPENING_ROUND_BRACKET, Constants.CLOSING_ROUND_BRACKET ->
                    {
                        //bracketType = BracketIndent.ROUND
                        openingBracket = Constants.OPENING_ROUND_BRACKET
                        closingBracket = Constants.CLOSING_ROUND_BRACKET
                    }

                    Constants.OPENING_SQUARE_BRACKET, Constants.CLOSING_SQUARE_BRACKET ->
                    {
                        //bracketType = BracketIndent.SQUARE
                        openingBracket = Constants.OPENING_SQUARE_BRACKET
                        closingBracket = Constants.CLOSING_SQUARE_BRACKET
                    }
                }

                if (token.text == openingBracket)
                {
                    println("  Token is $openingBracket")

                    if (currentStack.isEmpty())
                    {
                        println("    Current stack is empty => push $openingBracket to new stack")
                        newStack.push(BracketIndent(openingBracket, -1))
                    }
                    else
                    {
                        println("    Current stack is not empty")
                        if (currentStack.peek() is KeywordIndent)
                        {
                            println("      Current stack ends with keyword => replace with $openingBracket")
                            currentStack.pop()
                            newStack.push(BracketIndent(openingBracket, -1))
                        }
                        else
                        {
                            println("      Current stack does not end with keyword => push $openingBracket to new stack")
                            newStack.push(BracketIndent(openingBracket, -1))
                        }
                    }

                    println("      Current stack: \"" + Tools.toString(currentStack) + "\"")
                    println("      New stack:     \"" + Tools.toString(newStack) + "\"")
                    println("      Current line:  \"" + Tools.toDisplayString(currentLine) + "\"")
                    continue
                }

                if (token.text == closingBracket)
                {
                    println("  Token is $closingBracket")
                    if (wasCurrentLineEmpty)
                    {
                        println("    Current line is empty")
                        val currentStackTop = currentStack.lastOrNull()
                        if (currentStackTop is BracketIndent && currentStackTop.text == openingBracket)
                        {
                            println("      Current stack ends with $openingBracket => remove $openingBracket")
                            currentStack.pop()
                        }
                    }
                    else
                    {
                        println("    Current line is not empty")

                        val newStackTop = newStack.lastOrNull()
                        if (newStackTop is BracketIndent && newStackTop.text == openingBracket)
                        {
                            println("        New stack ends with $openingBracket => remove $openingBracket")
                            newStack.pop()
                        }
                        else
                        {
                            println("        New stack does not end with $openingBracket")

                            val currentStackTop = currentStack.lastOrNull()
                            if (currentStackTop is BracketIndent && currentStackTop.text == openingBracket)
                            {
                                println("      Current stack ends with $openingBracket => remove $openingBracket")
                                currentStack.pop()
                            }
                            else
                            {
                                TODO("Not covered by any test at all.")
                                println("      Current stack does not end with $openingBracket")
                            }
                        }
                    }

                    println("        Current stack: \"" + Tools.toString(currentStack) + "\"")
                    println("        New stack:     \"" + Tools.toString(newStack) + "\"")
                    println("        Current line:  \"" + Tools.toDisplayString(currentLine) + "\"")
                    continue
                }
            }

            if (token is LineBreakToken)
            {
                println("  Token is line break")
                println("    Current stack: \"" + Tools.toString(currentStack) + "\"")
                println("    New stack:     \"" + Tools.toString(newStack) + "\"")

                val currentStackTop = currentStack.lastOrNull()
                val currentLevel2 = currentStackTop?.level ?: 0
                val line = indentText(currentLine, currentLevel2)
                println("    => \"${Tools.toDisplayString(line)}\"")
                lines += line

                currentLine = ""

                var currentStackLevelModifier = 0
                val newStackTop = newStack.lastOrNull()
                if (newStackTop != null)
                {
                    if (newStack.size >= 2 && newStack[0] is KeywordIndent)
                    {
                        println("    New stack starts with keyword and has more entries => remove keyword")
                        newStack.removeAt(0)
                    }
                    else if (currentStackTop is KeywordIndent && newStack.size >= 1 && newStack[0] is BracketIndent)
                    {
                        TODO("Not covered by any test at all.")
                        println("    Current stack ends with keyword and new stack starts with bracket => remove keyword")
                        currentStack.removeLast()
                        currentStackLevelModifier--
                    }
                    else
                        println("    No stack modification")

                    for (item in newStack)
                        when (item)
                        {
                            is KeywordIndent -> currentStack += KeywordIndent(item.text, currentLevel2 + 1 + currentStackLevelModifier)
                            is BracketIndent -> currentStack += BracketIndent(item.text, currentLevel2 + 1 + currentStackLevelModifier)
                            else -> TODO()
                        }

                    newStack.clear()
                }

                val currentStackTop2 = currentStack.lastOrNull()
                currentLevel = (currentStackTop2?.level ?: 0) + currentStackLevelModifier

                println("\n    Current stack: \"" + Tools.toString(currentStack) + "\"")
                println("    Current line:  \"" + Tools.toDisplayString(currentLine) + "\"")
                println("    Next level:    $currentLevel\n")
                continue
            }

            println("  Token is other: ${token::class.simpleName}")
        }

        if (currentLine.isNotEmpty())
            lines += indentText(currentLine, currentStack.size)

        return IndentResult(lines, remainingTokens)
    }

    fun recreate(tokens: ArrayList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }

    private fun indentText(text: String, level: Int): String
    {
        //println("indentText: \"${Tools.toDisplayString(text)}\", level: $level")

        if (text.isBlank())
        {
            println("indentText: \"${Tools.toDisplayString(text)}\", level: $level is BLANK")
            return text
        }

        if (level < 0)
            throw IndenterException("level is negative: $level (text: \"${Tools.toDisplayString(text)}\")")

        val pad = " ".repeat(level * spacesPerLevel)

        //println("pad:    $pad<")
        //println("text:   ${Tools.toDisplayString(text)}<")
        val result = pad + text
        //println("result: ${Tools.toDisplayString(result)}<")
        return result
    }
}
