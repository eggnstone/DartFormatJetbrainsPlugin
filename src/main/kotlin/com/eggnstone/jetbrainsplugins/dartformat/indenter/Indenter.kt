package com.eggnstone.jetbrainsplugins.dartformat.indenter

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import com.eggnstone.jetbrainsplugins.dartformat.ToolsOld
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
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
        DotlinLogger.log("indentTokens: ${ToolsOld.tokensToDisplayString2(inputTokens)}")

        val lines = mutableListOf<String>()
        val remainingTokens = inputTokens.toMutableList()

        var currentLevel = 0
        var currentLine = ""
        val currentStack = Stack<IIndent>()
        val newStack = Stack<IIndent>()

        for (token in inputTokens)
        {
            //DotlinTools.println("  token: $token")
            remainingTokens.removeAt(0)

            val wasCurrentLineEmpty = currentLine.isEmpty()

            // Remove leading white space
            if (token is WhiteSpaceToken)
            {
                if (wasCurrentLineEmpty)
                {
                    DotlinLogger.log("  Token is white space & line is empty => ignore")
                    printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                    continue
                }
            }

            currentLine += token.recreate()

            if (token is ClassKeywordToken)
            {
                if (token.isMainClassKeyword && wasCurrentLineEmpty)
                {
                    DotlinLogger.log("  Token is main class keyword => push class keyword indent to new stack")
                    newStack.push(ClassKeywordIndent(token.text, -1))
                }

                printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                continue
            }

            if (token is KeywordToken)
            {
                if (wasCurrentLineEmpty)
                {
                    DotlinLogger.log("  Token is keyword => push keyword indent to new stack")
                    newStack.push(KeywordIndent(token.text, -1))
                }

                printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                continue
            }

            if (token is SpecialToken)
            {
                var openingBracket = ""
                var closingBracket = ""
                when (token.text)
                {
                    Constants.OPENING_CURLY_BRACKET, Constants.CLOSING_CURLY_BRACKET ->
                    {
                        openingBracket = Constants.OPENING_CURLY_BRACKET
                        closingBracket = Constants.CLOSING_CURLY_BRACKET
                    }

                    Constants.OPENING_ROUND_BRACKET, Constants.CLOSING_ROUND_BRACKET ->
                    {
                        openingBracket = Constants.OPENING_ROUND_BRACKET
                        closingBracket = Constants.CLOSING_ROUND_BRACKET
                    }

                    Constants.OPENING_SQUARE_BRACKET, Constants.CLOSING_SQUARE_BRACKET ->
                    {
                        openingBracket = Constants.OPENING_SQUARE_BRACKET
                        closingBracket = Constants.CLOSING_SQUARE_BRACKET
                    }
                }

                if (token.text == openingBracket)
                {
                    DotlinLogger.log("  Token is $openingBracket")

                    if (currentStack.isEmpty())
                    {
                        DotlinLogger.log("    Current stack is empty => push $openingBracket indent to new stack")
                        newStack.push(BracketIndent(openingBracket, -1))
                    }
                    else
                    {
                        DotlinLogger.log("    Current stack is not empty")
                        val currentStackTop = currentStack.lastOrNull()
                        if (currentStackTop is ClassKeywordIndent)
                        {
                            DotlinLogger.log("      Current stack ends with class keyword => replace with $openingBracket")
                            currentStack.pop()
                            newStack.push(BracketIndent(openingBracket, -1))
                        }
                        else if (currentStackTop is KeywordIndent)
                        {
                            DotlinLogger.log("      Current stack ends with keyword")
                            if (wasCurrentLineEmpty)
                            {
                                DotlinLogger.log("        Current line was empty => replace with $openingBracket")
                                currentStack.pop()
                                newStack.push(BracketIndent(openingBracket, -1))
                            }
                            else
                            {
                                DotlinLogger.log("        Current line was not empty => push $openingBracket indent to new stack")
                                newStack.push(BracketIndent(openingBracket, -1))
                            }
                        }
                        else
                        {
                            DotlinLogger.log("      Current stack does not end with keyword => push $openingBracket indent to new stack")
                            newStack.push(BracketIndent(openingBracket, -1))
                        }
                    }

                    printInfo("      ", currentLevel, currentLine, currentStack, newStack)
                    continue
                }

                if (token.text == closingBracket)
                {
                    DotlinLogger.log("  Token is $closingBracket")
                    if (wasCurrentLineEmpty)
                    {
                        DotlinLogger.log("    Current line is empty")
                        val currentStackTop = currentStack.lastOrNull()
                        if (currentStackTop is BracketIndent && currentStackTop.text == openingBracket)
                        {
                            DotlinLogger.log("      Current stack ends with $openingBracket => remove $openingBracket")
                            currentStack.pop()
                        }
                    }
                    else
                    {
                        DotlinLogger.log("    Current line is not empty")

                        val newStackTop = newStack.lastOrNull()
                        if (newStackTop is BracketIndent && newStackTop.text == openingBracket)
                        {
                            DotlinLogger.log("        New stack ends with $openingBracket => remove $openingBracket")
                            newStack.pop()

                            if (openingBracket == Constants.OPENING_CURLY_BRACKET
                                && currentStack.isEmpty()
                                && (newStack.lastOrNull() is ClassKeywordIndent || newStack.lastOrNull() is KeywordIndent)
                            )
                            {
                                DotlinLogger.log("        => remove (class) keyword, too")
                                newStack.pop()
                            }
                        }
                        else
                        {
                            DotlinLogger.log("        New stack does not end with $openingBracket")

                            val currentStackTop = currentStack.lastOrNull()
                            if (currentStackTop is BracketIndent && currentStackTop.text == openingBracket)
                            {
                                DotlinLogger.log("      Current stack ends with $openingBracket => remove $openingBracket")
                                currentStack.pop()
                            }
                            else
                            {
                                DotlinLogger.log(ToolsOld.tokensToDisplayString2(inputTokens))
                                printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                                TODO("Not covered by any test at all (1) " + ToolsOld.shorten(ToolsOld.tokensToDisplayString2(inputTokens), 1000))
                                DotlinLogger.log("      Current stack does not end with $openingBracket")
                            }
                        }
                    }

                    printInfo("        ", currentLevel, currentLine, currentStack, newStack)
                    continue
                }

                if (token.text == ";")
                {
                    DotlinLogger.log("  Token is ;")
                    printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                    val currentStackTop = currentStack.lastOrNull()
                    if (currentStackTop is KeywordIndent)
                    {
                        DotlinLogger.log("    Current stack ends with $currentStackTop => push removal indent to new stack")
                        newStack.push(RemovalIndent(1))
                    }
                    else
                    {
                        val newStackTop = newStack.lastOrNull()
                        if (newStackTop is KeywordIndent)
                        {
                            DotlinLogger.log("    New stack ends with $newStackTop => remove $newStackTop")
                            newStack.pop()
                        }
                    }

                    continue
                }
            }

            if (token is LineBreakToken)
            {
                DotlinLogger.log("  Token is line break")
                printInfo("    ", currentLevel, currentLine, currentStack, newStack)

                val currentStackTop = currentStack.lastOrNull()
                val currentLevel2 = currentStackTop?.level ?: 0
                val line = indentText(currentLine, currentLevel2)
                DotlinLogger.log("    -> ${ToolsOld.toDisplayString2(line)}")
                lines += line

                currentLine = ""

                var currentStackLevelModifier = 0
                val newStackBottom = newStack.firstOrNull()
                if (newStackBottom != null)
                {
                    if (newStack.size >= 2 && (newStackBottom is ClassKeywordIndent || newStackBottom is KeywordIndent))
                    {
                        DotlinLogger.log("    New stack starts with (class) keyword and has more entries => remove (class) keyword")
                        newStack.removeAt(0)
                    }
                    else if (currentStackTop is KeywordIndent && newStackBottom is BracketIndent)
                    {
                        DotlinLogger.log("    Current stack ends with keyword and new stack starts with bracket => remove keyword")
                        currentStack.removeLast()
                        currentStackLevelModifier--
                    }
                    else
                        DotlinLogger.log("    No stack modification")

                    for (item in newStack)
                        when (item)
                        {
                            is BracketIndent -> currentStack.add(BracketIndent(item.text, currentLevel2 + 1 + currentStackLevelModifier)) // dotlin
                            is ClassKeywordIndent -> currentStack.add(KeywordIndent(item.text, currentLevel2 + 1 + currentStackLevelModifier)) // dotlin
                            is KeywordIndent -> currentStack.add(KeywordIndent(item.text, currentLevel2 + 1 + currentStackLevelModifier)) // dotlin
                            is RemovalIndent -> currentStack.pop()
                            else -> throw DartFormatException("Unexpected type: ${item::class.simpleName}")
                        }

                    newStack.clear()
                }

                val currentStackTop2 = currentStack.lastOrNull()
                currentLevel = (currentStackTop2?.level ?: 0) + currentStackLevelModifier

                DotlinLogger.log("")
                printInfo("    ", currentLevel, currentLine, currentStack, newStack)
                DotlinLogger.log("")
                continue
            }

            DotlinLogger.log("  Token is other: ${token::class.simpleName}")
        }

        if (currentLine.isNotEmpty())
            lines.add(indentText(currentLine, currentStack.size)) // dotlin

        return IndentResult(lines, remainingTokens)
    }

    private fun printInfo(spacer: String, currentLevel: Int, currentLine: String, currentStack: Stack<IIndent>, newStack: Stack<IIndent>)
    {
        DotlinLogger.log("${spacer}Current level: $currentLevel")
        DotlinLogger.log("${spacer}Current line:  " + ToolsOld.toDisplayString2(currentLine))
        DotlinLogger.log("${spacer}Current stack: " + ToolsOld.indentsToDisplayString2(currentStack))
        DotlinLogger.log("${spacer}New stack:     " + ToolsOld.indentsToDisplayString2(newStack))
    }

    fun recreate(tokens: MutableList<IToken>): String
    {
        val sb = StringBuilder()

        for (token in tokens)
            sb.append(token.recreate())

        return sb.toString()
    }

    private fun indentText(text: String, level: Int): String
    {
        //DotlinTools.println("indentText: ${Tools.toDisplayString2(text)}, level: $level")

        if (text.isBlank())
        {
            DotlinLogger.log("indentText: ${ToolsOld.toDisplayString2(text)}, level: $level is BLANK")
            return text
        }

        if (level < 0)
            throw DartFormatException("level is negative: $level (text: ${ToolsOld.toDisplayString2(text)})")

        val pad = " ".repeat(level * spacesPerLevel)

        //DotlinTools.println("pad:    $pad<")
        //DotlinTools.println("text:   ${Tools.toDisplayString(text)}<")

        @Suppress("UnnecessaryVariable")
        val result = pad + text
        //DotlinTools.println("result: ${Tools.toDisplayString(result)}<")

        return result
    }
}
