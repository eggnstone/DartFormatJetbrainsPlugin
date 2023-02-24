package com.eggnstone.jetbrainsplugins.dartformat.blockifier

import com.eggnstone.jetbrainsplugins.dartformat.blocks.*

class State
{
    val blocks = arrayListOf<IBlock>()

    var isInClass = false
    var isInClassHeader = false
    var isInClassBody = false

    //var isInExpression = false

    var isInWhitespace = true

    var currentClassHeader = ""

    var currentText = ""

    //var currentBody = ""
    //var currentFooter = ""
}

class Blockifier
{
    fun blockify(text: String): ArrayList<IBlock>
    {
        var state = State()

        for (c in text)
        {
            if (state.isInClass)
            {
                state = handleInClass(c, state)
                continue
            }

            if (state.isInWhitespace)
            {
                state = handleInWhitespace(c, state)
                continue
            }

            if (isWhitespace(c))
            {
                if (state.currentText.isEmpty())
                {
                    state.isInWhitespace = true
                    state.currentText += c
                    continue
                }

                if (state.currentText == "class" || state.currentText == "abstract class")
                {
                    state.isInClass = true
                    state.isInClassHeader = true
                }

                state.currentText += c
                continue
            }

            if (c == ';')
            {
                state.blocks += ExpressionBlock(state.currentText + c)
                state.currentText = ""
                continue
            }

            state.currentText += c
        }

        /*if (state.currentText.isNotEmpty())
            TODO("state.currentText.isNotEmpty()")
        if (state.isInClass)
            TODO("state.isInClass")*/

        return state.blocks
    }

    private fun handleInClass(c: Char, state: State): State
    {
        if (state.isInClassHeader)
        {
            if (c == '{')
            {
                state.isInClassHeader = false
                state.isInClassBody = true
                state.currentClassHeader = state.currentText
                state.currentText = ""
            }

            state.currentText += c
            return state
        }

        if (state.isInClassBody)
        {
            if (c == '}')
            {
                state.isInClass = false
                state.isInClassBody = false
                val innerBlock = UnknownBlock(state.currentText.substring(1))
                state.blocks += ClassBlock(state.currentClassHeader, arrayListOf(innerBlock))
                state.currentClassHeader = ""
                state.currentText = ""
                return state
            }

            state.currentText += c
            return state
        }

        state.currentText += c
        return state
    }

    private fun handleInWhitespace(c: Char, state: State): State
    {
        if (isWhitespace(c))
        {
            state.currentText += c
            return state
        }

        state.isInWhitespace = false

        if (state.currentText.isNotEmpty())
            state.blocks += WhitespaceBlock(state.currentText)

        state.currentText = c.toString()
        return state
    }

    private fun isWhitespace(c: Char): Boolean = (c == ' ' || c == '\n' || c == '\r' || c == '\t')
}
