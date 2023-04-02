package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.levels.BracketPackage
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart

class Tools
{
    companion object
    {
        private const val closingBrackets = "])}"
        private const val openingBrackets = "{[("

        fun containsLineBreak(s: String) = StringWrapper.containsChar(s, "\n") || StringWrapper.containsChar(s, "\r")

        fun countLeadingSpaces(s: String): Int
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
                val c = s.get(i).toString() // workaround for dotlin for: for (c in text)
                if (c != " ")
                    return i
            }

            return s.length
        }

        fun getClosingBracket(closingBracket: String): String
        {
            @Suppress("LiftReturnOrAssignment") // dotlin
            when (closingBracket)
            {
                "{" -> return "}"
                "(" -> return ")"
                "[" -> return "]"
                else -> throw DartFormatException("Tools.getClosingBracket: Unexpected closing bracket: $closingBracket")
            }
        }

        fun getElseEndPos(s: String): Int = getTextEndPos(s, "else")

        fun getTextEndPos(s: String, searchText: String): Int
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("getTextEndPos(${toDisplayStringShort(s)}, ${toDisplayString(searchText)})")

            if (s == searchText)
                return s.length

            val pos = StringWrapper.indexOf(s, searchText)
            if (pos == -1)
                return -1

            val leadingText = StringWrapper.substring(s, 0, pos)
            if (!StringWrapper.isBlank(leadingText))
                return -1

            val trailingText = StringWrapper.substring(s, pos + searchText.length)
            DotlinLogger.log("trailingText: ${toDisplayString(trailingText)}")
            if (StringWrapper.isEmpty(trailingText))
                return s.length

            if (StringWrapper.isBlank(trailingText))
                return s.length

            @Suppress("ReplaceGetOrSet") // dotlin
            val cFirst = trailingText.get(0).toString()
            if (!isWhitespace(cFirst) && cFirst != "{" && cFirst != ":")
                return -1

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until trailingText.length)
            {
                @Suppress("ReplaceGetOrSet") // dotlin
                val c = trailingText.get(i).toString()
                if (!isWhitespace(c) || c == ":")
                    return pos + searchText.length + i
            }

            TODO("cannot be reached") // return -1
        }

        fun getIndentOfLastLine(s: String): Int = countLeadingSpaces(getLastLine(s))

        fun getLastLine(s: String): String
        {
            if (StringWrapper.isEmpty(s))
                return ""

            val lastChar = StringWrapper.last(s)
            if (lastChar == "\n" || lastChar == "\r")
                return ""

            val lastN = s.lastIndexOf("\n")
            val lastR = s.lastIndexOf("\r")
            val lastLineBreakPos = DotlinTools.maxOf(lastN, lastR)
            if (lastLineBreakPos == -1)
                return s

            return StringWrapper.substring(s, lastLineBreakPos + 1)
        }

        fun getNextLinePos(s: String): Int
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("getNextLinePos(${toDisplayStringShort(s)})")

            val nrPos = StringWrapper.indexOf(s, "\n\r")
            val rnPos = StringWrapper.indexOf(s, "\r\n")

            if (nrPos >= 0 && (rnPos < 0 || nrPos < rnPos))
                return nrPos + 2

            if (rnPos >= 0 && (nrPos < 0 || rnPos < nrPos))
                return rnPos + 2

            val nPos = StringWrapper.indexOf(s, "\n")
            if (nPos >= 0)
                return nPos + 1

            val rPos = StringWrapper.indexOf(s, "\r")
            if (rPos >= 0)
                return rPos + 1

            return -1
        }

        fun isBracket(c: String): Boolean = StringWrapper.containsChar(openingBrackets + closingBrackets, c)

        fun isClosingBracket(c: String): Boolean = StringWrapper.containsChar(closingBrackets, c)

        fun isOpeningBracket(c: String): Boolean = StringWrapper.containsChar(openingBrackets, c)

        fun isWhitespace(c: String): Boolean = StringWrapper.containsChar("\n\r\t ", c)

        @Suppress("MemberVisibilityCanBePrivate")
        fun shorten(s: String, maxLength: Int, addEllipsis: Boolean): String
        {
            if (s.length < maxLength)
                return s

            if (addEllipsis)
                return StringWrapper.substring(s, 0, maxLength - 4) + " ..." // dotlin

            TODO("Tools.shorten") // return StringWrapper.substring(s, 0, maxLength) // dotlin
            //return s.substring(0, maxLength)
        }

        fun shorten100(s: String): String = shorten(s, 100, true)

        fun toDisplayString(s: String): String = "\"" + toDisplayStringSimple(s) + "\""

        fun toDisplayStringShort(s: String): String = toDisplayString(shorten100(s))

        fun toDisplayStringSimple(s: String): String = StringWrapper.replace(StringWrapper.replace(s, "\r", "\\r"), "\n", "\\n")

        fun toDisplayStringForBracketPackages(bracketPackages: List<BracketPackage>): String = "[" + toDisplayStringForBracketPackagesInternal(bracketPackages) + "]"

        fun toDisplayStringForPartLists(partLists: List<List<IPart>>): String = "[" + toDisplayStringForPartListsInternal(partLists) + "]"

        fun toDisplayStringForParts(parts: List<IPart>): String = "[" + toDisplayStringForPartsInternal(parts) + "]"

        fun toDisplayStringForStrings(strings: List<String>): String = "[" + toDisplayStringForStringsInternal(strings) + "]"

        fun trimSimple(s: String): String = trimStartSimple(trimEndSimple(s))

        fun trimStartSimple(s: String): String
        {
            var startText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
                val c = s.get(i).toString() // workaround for dotlin for: for (c in text)
                if (c == "\n" || c == "\r")
                {
                    startText += c
                    continue
                }

                if (c == "\t" || c == " ")
                    continue

                return startText + StringWrapper.substring(s, i)
            }

            return startText
        }

        fun trimEndSimple(s: String): String
        {
            var endText = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in s.length - 1 downTo 0)
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin for: for (c in text)
                val c = s.get(i).toString() // workaround for dotlin for: for (c in text)
                if (c == "\n" || c == "\r")
                {
                    endText = c + endText
                    continue
                }

                if (c == "\t" || c == " ")
                    continue

                return StringWrapper.substring(s, 0, i + 1) + endText
            }

            return endText
        }

        private fun toDisplayStringForPartListsInternal(partLists: List<List<IPart>>): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
            for (i in 0 until partLists.size) // workaround for dotlin
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin
                val parts = partLists.get(i) // workaround for dotlin
                if (StringWrapper.isNotEmpty(result))
                    result += ","

                result += toDisplayStringForParts(parts)
            }

            return result
        }

        private fun toDisplayStringForPartsInternal(parts: List<IPart>): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
            for (i in 0 until parts.size) // workaround for dotlin
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin
                val part = parts.get(i) // workaround for dotlin
                if (StringWrapper.isNotEmpty(result))
                    result += ","

                result += toDisplayStringSimple(part.toString())
            }

            return result
        }

        private fun toDisplayStringForBracketPackagesInternal(bracketPackages: List<BracketPackage>): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
            for (i in 0 until bracketPackages.size) // workaround for dotlin
            {
                @Suppress("ReplaceGetOrSet") // workaround for dotlin
                val bracketPackage = bracketPackages.get(i) // workaround for dotlin
                if (StringWrapper.isNotEmpty(result))
                    result += ","

                result += toDisplayStringSimple(bracketPackage.toString())
            }

            return result
        }

        private fun toDisplayStringForStringsInternal(strings: List<String>): String
        {
            var result = ""

            /* dotlin
            for (s in strings)
                result += toDisplayString1(s)
            */

            @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
            for (i in 0 until strings.size)
            {
                if (i > 0)
                    result += ","

                result += toDisplayString(strings[i])
            }

            // dotlin
            //return toDisplayString1(strings.joinToString(separator = "") { it })

            return result
        }
    }
}
