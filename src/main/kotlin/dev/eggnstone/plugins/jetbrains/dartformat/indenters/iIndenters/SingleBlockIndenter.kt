package dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.Tools
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.StringWrapper
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

        //if (DotlinLogger.isEnabled) DotlinLogger.log("parts: ${Tools.toDisplayStringForParts(singleBlock.parts)}")

        val blockIndenter = BlockIndenter(spacesPerLevel)
        val indentedBody = blockIndenter.indentParts(singleBlock.parts, spacesPerLevel)

        @Suppress("UnnecessaryVariable")
        val result = header + indentedBody + footer

        return result
    }

    fun indentHeader(header: String): String
    {
        if (StringWrapper.isEmpty(header))
            throw DartFormatException("Unexpected empty header.")

        if (!StringWrapper.endsWith(header, "{"))
            throw DartFormatException("Unexpected header end: " + Tools.toDisplayString(StringWrapper.substring(header, header.length - 1)))

        val shortenedHeader = StringWrapper.substring(header, 0, header.length - 1)

        val headerLines = lineSplitter.split(shortenedHeader, true)
        if (headerLines.isEmpty())
            TODO("untested") // return "{"

        var usesColon = false
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

        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until headerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val headerLine = headerLines.get(i) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("headerLine #$i: ${Tools.toDisplayString(headerLine)}")

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
                TODO("untested")

            var pad = StringWrapper.getSpaces(spacesPerLevel)

            if (usesColon && !startsWithColon)
                pad = "$pad  "

            result += pad + headerLine
        }

        // TODO: find a better solution
        val endsWithWhitespace = StringWrapper.isNotEmpty(result) && Tools.isWhitespace(StringWrapper.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"

        return result
    }

    fun indentFooter(footer: String): String
    {
        if (!StringWrapper.startsWith(footer, "}"))
            throw DartFormatException("Footer must start with closing brace: ${Tools.toDisplayString(footer)}")

        val footerLines = lineSplitter.split(footer, true)
        var result = StringBuffer(footerLines[0])
        var startIndex = 1
        var isInMultiLineComment = false

        // Fix leading comments and "else"
        while (startIndex < footerLines.size)
        {
            val currentLine = footerLines[startIndex]
            if (DotlinLogger.isEnabled) DotlinLogger.log("currentLine:  ${Tools.toDisplayString(currentLine)}")

            if (isInMultiLineComment)
            {
                TODO("untested")
                result.append(currentLine)
                startIndex++

                if (StringWrapper.containsString(currentLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(currentLine, "/*"))
            {
                TODO("untested")
                result.append(currentLine)
                startIndex++

                if (!StringWrapper.containsString(currentLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(currentLine, "//"))
            {
                TODO("untested")
                result.append(currentLine)
                startIndex++
                continue
            }

            if (StringWrapper.trim(currentLine) == "}")
            {
                TODO("untested")
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

        if (DotlinLogger.isEnabled) DotlinLogger.log("startIndex: $startIndex")
        if (DotlinLogger.isEnabled) DotlinLogger.log("footerLines.size: ${footerLines.size}")
        @Suppress("ReplaceManualRangeWithIndicesCalls") // workaround for dotlin
        for (i in startIndex until footerLines.size) // workaround for dotlin
        {
            @Suppress("ReplaceGetOrSet") // workaround for dotlin
            val footerLine = footerLines.get(i) // workaround for dotlin
            if (DotlinLogger.isEnabled) DotlinLogger.log("headerLine #$i: ${Tools.toDisplayString(footerLine)}")

            if (isInMultiLineComment)
            {
                TODO("untested")
                result.append(footerLine)

                if (StringWrapper.containsString(footerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(footerLine, "/*"))
            {
                TODO("untested")
                // no padding for multi line comments
                result.append(footerLine)

                if (!StringWrapper.containsString(footerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(footerLine, "//"))
            {
                TODO("untested")
                // no padding for end of line comments
                result.append(footerLine)
                continue
            }

            if (StringWrapper.isBlank(footerLine))
                TODO("untested")

            val pad = StringWrapper.getSpaces(spacesPerLevel)
            result.append(pad + footerLine)
        }

        /*// TODO: find a better solution
        val endsWithWhitespace = DotlinTools.isNotEmpty(result) && Tools.isWhitespace(StringWrapper.substring(result, result.length - 1))
        result += if (endsWithWhitespace) "{" else " {"*/

        return result.toString()
    }
}
