package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinTools
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.BlockIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.parts.IPart
import dev.eggnstone.plugins.jetbrains.dartformat.parts.SingleBlock
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.LineSplitter

class SingleBlockIndenter(private val spacesPerLevel: Int) : IIndenter
{
    companion object
    {
        private val lineSplitter = LineSplitter()
    }

    override fun indentPart(part: IPart): String
    {
        if (part !is SingleBlock)
            throw DartFormatException("Unexpected non-SingleBlock type.")

        val singleBlock: SingleBlock = part

        val header = indentHeader(singleBlock.header)
        val footer = indentFooter(singleBlock.footer)

        //DotlinLogger.log("parts: ${Tools.toDisplayStringForParts(singleBlock.parts)}")

        val blockIndenter = BlockIndenter(spacesPerLevel)
        val indentedBody = blockIndenter.indentParts(singleBlock.parts, spacesPerLevel)

        @Suppress("UnnecessaryVariable")
        val result = header + indentedBody + footer

        return result
    }

    fun indentHeader(header: String): String
    {
        if (DotlinTools.isEmpty(header))
            throw DartFormatException("Unexpected empty header.")

        if (!DotlinTools.endsWith(header, "{"))
            throw DartFormatException("Unexpected header end: " + Tools.toDisplayString(DotlinTools.substring(header, header.length - 1)))

        val shortenedHeader = DotlinTools.substring(header, 0, header.length - 1)

        val headerLines = lineSplitter.split(shortenedHeader, true)
        if (headerLines.isEmpty())
            return "{"

        var result = headerLines[0]

        var startIndex = 1
        var isInMultiLineComment = false

        // Fix annotations and leading comments
        while (startIndex < headerLines.size)
        {
            val previousLine = headerLines[startIndex - 1]
            val currentLine = headerLines[startIndex]

            if (isInMultiLineComment)
            {
                result += currentLine
                startIndex++

                if (DotlinTools.containsString(previousLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (DotlinTools.startsWith(previousLine, "/*"))
            {
                result += currentLine
                startIndex++

                if (!DotlinTools.containsString(previousLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (DotlinTools.startsWith(previousLine, "//"))
            {
                result += currentLine
                startIndex++
                continue
            }

            if (DotlinTools.startsWith(previousLine, "@"))
            {
                result += currentLine
                startIndex++
                continue
            }

            break
        }

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until headerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val headerLine = headerLines.get(i) // workaround for dotlin
            DotlinLogger.log("headerLine #$i: ${Tools.toDisplayString(headerLine)}")

            if (isInMultiLineComment)
            {
                result += headerLine

                if (DotlinTools.containsString(headerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (DotlinTools.startsWith(headerLine, "/*"))
            {
                // no padding for multi line comments
                result += headerLine

                if (!DotlinTools.containsString(headerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (DotlinTools.startsWith(headerLine, "//"))
            {
                // no padding for end of line comments
                result += headerLine
                continue
            }

            // TODO: find a better solution
            //if (DotlinTools.startsWith(headerLine, "async ") || headerLine == "async")//DotlinTools.trimEnd(headerLine) == "async"))
            if (DotlinTools.startsWith(headerLine, "async ")
                || DotlinTools.startsWith(headerLine, "async\t")
                || DotlinTools.startsWith(headerLine, "async\n")
                || DotlinTools.startsWith(headerLine, "async\r")
                || headerLine == "async"
            )
            {
                // no padding for "async..."
                result += headerLine
                continue
            }

            if (DotlinTools.isBlank(headerLine))
                TODO("untested")

            val pad = DotlinTools.getSpaces(spacesPerLevel)
            result += pad + headerLine
        }

        // TODO: find a better solution
        val endsWithWhitespace = DotlinTools.isNotEmpty(result) && Tools.isWhitespace(DotlinTools.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"

        return result
    }

    fun indentFooter(footer: String): String
    {
        if (!footer.startsWith("}"))
            throw DartFormatException("Footer must start with closing brace: ${Tools.toDisplayString(footer)}")

        val footerLines = lineSplitter.split(footer, true)
        var result = footerLines[0]
        var startIndex = 1
        var isInMultiLineComment = false

        // Fix leading comments and "else"
        while (startIndex < footerLines.size)
        {
            val currentLine = footerLines[startIndex]
            DotlinLogger.log("currentLine:  ${Tools.toDisplayString(currentLine)}")

            if (isInMultiLineComment)
            {
                TODO("untested")
                result += currentLine
                startIndex++

                if (DotlinTools.containsString(currentLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (DotlinTools.startsWith(currentLine, "/*"))
            {
                TODO("untested")
                result += currentLine
                startIndex++

                if (!DotlinTools.containsString(currentLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (DotlinTools.startsWith(currentLine, "//"))
            {
                TODO("untested")
                result += currentLine
                startIndex++
                continue
            }

            if (DotlinTools.trim(currentLine) == "}")
            {
                result += currentLine
                startIndex++
                continue
            }

            val pos = Tools.getElseEndPos(currentLine)
            DotlinLogger.log("pos: $pos")
            if (pos >= 0)
            {
                result += currentLine
                startIndex++
                continue
            }

            break
        }

        DotlinLogger.log("startIndex: $startIndex")
        DotlinLogger.log("footerLines.size: ${footerLines.size}")
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until footerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val footerLine = footerLines.get(i) // workaround for dotlin
            DotlinLogger.log("headerLine #$i: ${Tools.toDisplayString(footerLine)}")

            if (isInMultiLineComment)
            {
                TODO("untested")
                result += footerLine

                if (DotlinTools.containsString(footerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (DotlinTools.startsWith(footerLine, "/*"))
            {
                TODO("untested")
                // no padding for multi line comments
                result += footerLine

                if (!DotlinTools.containsString(footerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (DotlinTools.startsWith(footerLine, "//"))
            {
                TODO("untested")
                // no padding for end of line comments
                result += footerLine
                continue
            }

            if (DotlinTools.isBlank(footerLine))
                TODO("untested")

            val pad = DotlinTools.getSpaces(spacesPerLevel)
            result += pad + footerLine
        }

        /*// TODO: find a better solution
        val endsWithWhitespace = DotlinTools.isNotEmpty(result) && Tools.isWhitespace(DotlinTools.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"*/

        return result
    }
}
