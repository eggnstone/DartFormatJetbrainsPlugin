package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.BlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.MultiBlock
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Tools

class MultiBlockIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val lineSplitter = LineSplitter()
    }

    private val blockIndenter = BlockIndenter(spacesPerLevel)

    override fun indentPart(part: IPart, startIndent: Int, indentLevel: Int): String
    {
        if (part !is MultiBlock)
            throw DartFormatException("Unexpected non-DoubleBlock type.")

        val multiBlock: MultiBlock = part
        if (multiBlock.headers.size != multiBlock.partLists.size)
            throw DartFormatException("headers.size != parts.size")

        val result = StringBuilder()

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until multiBlock.headers.size)
        {
            @Suppress("ReplaceGetOrSet") // dotlin
            val header = multiBlock.headers.get(i)
            val indentedHeader = indentHeader(header, i == 0)

            /*val indentedHeader = if (i == 0)
            {
                val indentedHeader = if (header.startsWith("abstract class ") || header.startsWith("class "))
                    indentHeader(header)
                else
                    header
            }
            else
                header*/

            result.append(indentedHeader)

            @Suppress("ReplaceGetOrSet") // dotlin
            val parts = multiBlock.partLists.get(i)
            val indentedParts = blockIndenter.indentParts(parts, spacesPerLevel)
            result.append(indentedParts)
        }

        /*val footer = multiBlock.footer
        val indentedFooter = if (false)
            indentFooter(footer)
        else
            footer

        result.append(indentedFooter)*/

        val footer = multiBlock.footer
        val indentedFooter = indentFooter(footer)
        result.append(indentedFooter)

        //result.append(multiBlock.footer)

        return result.toString()
    }

    fun indentHeader(header: String, isFirst: Boolean = true): String
    {
        if (!isFirst)
            return header

        return indentHeaderInternal(header)
    }

    private fun indentHeaderInternal(header: String): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("MultiBlockIndenter.indentHeader: ${Tools.toDisplayStringShort(header)}")

        if (StringWrapper.isEmpty(header))
            throw DartFormatException("Unexpected empty header.")

        if (!StringWrapper.endsWith(header, "{"))
            throw DartFormatException("Unexpected header end: " + Tools.toDisplayStringShort(StringWrapper.substring(header, header.length - 1)))

        val shortenedHeader = StringWrapper.substring(header, 0, header.length - 1)

        val headerLines = lineSplitter.split(shortenedHeader, trimStart = true, trimEnd = false)
        if (headerLines.isEmpty())
            return "{"

        var usesColon = false
        var result = headerLines[0]

        var startIndex = 1
        var isInMultiLineComment = false

        if (DotlinLogger.isEnabled) DotlinLogger.log("  headerLine #0: ${Tools.toDisplayStringShort(headerLines[0])}")

        // Fix annotations and leading comments
        while (startIndex < headerLines.size)
        {
            val previousLine = headerLines[startIndex - 1]
            val currentLine = headerLines[startIndex]
            if (DotlinLogger.isEnabled) DotlinLogger.log("  headerLine #$startIndex: ${Tools.toDisplayStringShort(currentLine)}")

            if (isInMultiLineComment)
            {
                result += currentLine
                startIndex++

                if (StringWrapper.containsString(previousLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(previousLine, "/*"))
            {
                result += currentLine
                startIndex++

                if (!StringWrapper.containsString(previousLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(previousLine, "//"))
            {
                result += currentLine
                startIndex++
                continue
            }

            if (StringWrapper.startsWith(previousLine, "@"))
            {
                result += currentLine
                startIndex++
                continue
            }

            break
        }

        if (DotlinLogger.isEnabled)
        {
            DotlinLogger.log("  startIndex: $startIndex")
            DotlinLogger.log("  headerLines.size: ${headerLines.size}")
        }

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until headerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val headerLine = headerLines.get(i) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("  headerLine #$i: ${Tools.toDisplayStringShort(headerLine)}")

            if (isInMultiLineComment)
            {
                result += headerLine

                if (StringWrapper.containsString(headerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(headerLine, "/*"))
            {
                // no padding for multi line comments
                result += headerLine

                if (!StringWrapper.containsString(headerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(headerLine, "//"))
            {
                // no padding for end of line comments
                result += headerLine
                continue
            }

            var startsWithColon = false
            if (!usesColon)
            {
                startsWithColon = StringWrapper.startsWith(headerLine, ":")
                if (startsWithColon)
                    usesColon = true
            }

            // TODO: find a better solution
            //if (StringWrapper.startsWith(headerLine, "async ") || headerLine == "async")//DotlinTools.trimEnd(headerLine) == "async"))
            if (StringWrapper.startsWith(headerLine, "async ")
                || StringWrapper.startsWith(headerLine, "async\t")
                || StringWrapper.startsWith(headerLine, "async\n")
                || StringWrapper.startsWith(headerLine, "async\r")
                || headerLine == "async"
            )
            {
                // no padding for "async..."
                result += headerLine
                continue
            }

            if (StringWrapper.isBlank(headerLine))
            {
                result += Tools.trimSimple(headerLine)
                continue
            }

            var pad = StringWrapper.getSpaces(spacesPerLevel)

            if (usesColon && !startsWithColon)
                pad = "$pad  "

            result += pad + headerLine
        }

        result += "{"
        if (DotlinLogger.isEnabled) DotlinLogger.log("  result: ${Tools.toDisplayStringShort(result)}")

        return result
    }

    fun indentFooter(footer: String): String
    {
        if (DotlinLogger.isEnabled) DotlinLogger.log("MultiBlockIndenter.indentFooter: ${Tools.toDisplayStringShort(footer)}")

        if (!StringWrapper.startsWith(footer, "}"))
            throw DartFormatException("Footer must start with closing brace: ${Tools.toDisplayStringShort(footer)}")

        val footerLines = lineSplitter.split(footer, trim = true)
        val result = StringBuffer(footerLines[0])
        var startIndex = 1
        var isInMultiLineComment = false

        if (DotlinLogger.isEnabled) DotlinLogger.log("footerLine #0: ${Tools.toDisplayStringShort(footerLines[0])}")

        // Fix leading comments and "else"
        while (startIndex < footerLines.size)
        {
            val currentLine = footerLines[startIndex]
            if (DotlinLogger.isEnabled) DotlinLogger.log("footerLine #$startIndex: ${Tools.toDisplayStringShort(currentLine)}")

            if (isInMultiLineComment)
            {
                TODO("SingleBlockIndenter.indentFooter 1")
                result.append(currentLine)
                startIndex++

                if (StringWrapper.containsString(currentLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(currentLine, "/*"))
            {
                TODO("SingleBlockIndenter.indentFooter 2")
                result.append(currentLine)
                startIndex++

                if (!StringWrapper.containsString(currentLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(currentLine, "//"))
            {
                TODO("SingleBlockIndenter.indentFooter 3")
                result.append(currentLine)
                startIndex++
                continue
            }

            if (StringWrapper.trim(currentLine) == "}")
            {
                TODO("SingleBlockIndenter.indentFooter 4")
                result.append(currentLine)
                startIndex++
                continue
            }

            val pos = Tools.getElseEndPos(currentLine)
            if (DotlinLogger.isEnabled) DotlinLogger.log("pos: $pos")
            if (pos >= 0)
            {
                result.append(currentLine)
                startIndex++
                continue
            }

            break
        }

        if (DotlinLogger.isEnabled)
        {
            DotlinLogger.log("  startIndex: $startIndex")
            DotlinLogger.log("  footerLines.size: ${footerLines.size}")
        }

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until footerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val footerLine = footerLines.get(i) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("footerLine #$i: ${Tools.toDisplayStringShort(footerLine)}")

            if (isInMultiLineComment)
            {
                TODO("SingleBlockIndenter.indentFooter 5")
                result.append(footerLine)

                if (StringWrapper.containsString(footerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(footerLine, "/*"))
            {
                TODO("SingleBlockIndenter.indentFooter 6")
                // no padding for multi line comments
                result.append(footerLine)

                if (!StringWrapper.containsString(footerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(footerLine, "//"))
            {
                TODO("SingleBlockIndenter.indentFooter 7")
                // no padding for end of line comments
                result.append(footerLine)
                continue
            }

            if (StringWrapper.isBlank(footerLine))
                TODO("SingleBlockIndenter.indentFooter 8")

            val pad = StringWrapper.getSpaces(spacesPerLevel)
            result.append(pad + footerLine)
        }

        /*// TODO: find a better solution
        val endsWithWhitespace = DotlinTools.isNotEmpty(result) && Tools.isWhitespace(StringWrapper.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"*/

        if (DotlinLogger.isEnabled) DotlinLogger.log("  result: ${Tools.toDisplayStringShort(result.toString())}")

        return result.toString()
    }
}
