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
        if (DotlinLogger.isEnabled) DotlinLogger.log("MultiBlockIndenter.indentPart: $part")

        if (part !is MultiBlock)
            throw DartFormatException("Unexpected non-MultiBlock type.")

        val multiBlock: MultiBlock = part

        val result = StringBuilder()

        @Suppress("ReplaceManualRangeWithIndicesCalls") // dotlin
        for (i in 0 until multiBlock.headers.size)
        {
            @Suppress("ReplaceGetOrSet") // dotlin
            val header = multiBlock.headers.get(i)
            if (DotlinLogger.isEnabled) DotlinLogger.log("  header #$i: ${Tools.toDisplayString(header)}")

            val indentedHeader = indentHeader(header)
            if (DotlinLogger.isEnabled) DotlinLogger.log("  indentedHeader: ${Tools.toDisplayString(indentedHeader)}")

            result.append(indentedHeader)

            @Suppress("ReplaceGetOrSet") // dotlin
            val parts = multiBlock.partLists.get(i)
            val indentedParts = blockIndenter.indentParts(parts)
            result.append(indentedParts)
        }

        val footer = multiBlock.footer
        if (DotlinLogger.isEnabled) DotlinLogger.log("  footer: ${Tools.toDisplayString(footer)}")
        val indentedFooter = indentFooter(footer)
        if (DotlinLogger.isEnabled) DotlinLogger.log("  indentedFooter: ${Tools.toDisplayString(indentedFooter)}")
        result.append(indentedFooter)

        return result.toString()
    }

    fun indentHeader(header: String): String
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
        val result = StringBuffer(headerLines[0])

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
                result.append(currentLine)
                startIndex++

                if (StringWrapper.containsString(previousLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(previousLine, "/*"))
            {
                result.append(currentLine)
                startIndex++

                if (!StringWrapper.containsString(previousLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(previousLine, "//"))
            {
                result.append(currentLine)
                startIndex++
                continue
            }

            if (StringWrapper.startsWith(previousLine, "@"))
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
                result.append(headerLine)

                if (StringWrapper.containsString(headerLine, "*/"))
                    isInMultiLineComment = false

                continue
            }

            if (StringWrapper.startsWith(headerLine, "/*"))
            {
                // no padding for multi line comments
                result.append(headerLine)

                if (!StringWrapper.containsString(headerLine, "*/"))
                    isInMultiLineComment = true

                continue
            }

            if (StringWrapper.startsWith(headerLine, "//"))
            {
                // no padding for end of line comments
                result.append(headerLine)
                continue
            }

            var startsWithColon = false
            if (!usesColon)
            {
                startsWithColon = StringWrapper.startsWith(headerLine, ":")
                if (startsWithColon)
                    usesColon = true
            }

            val asyncEndPos = Tools.getTextEndPos(headerLine, "async")
            if (asyncEndPos >= 0)
            {
                // no padding for "async..."
                result.append(headerLine)
                continue
            }

            val elseEndPos = Tools.getElseEndPos(headerLine)
            if (elseEndPos >= 0)
            {
                // no padding for "else..."
                result.append(headerLine)
                continue
            }

            if (StringWrapper.isBlank(headerLine))
            {
                result.append(Tools.trimSimple(headerLine))
                continue
            }

            var pad = StringWrapper.getSpaces(spacesPerLevel)

            if (usesColon && !startsWithColon)
                pad = "$pad  "

            result.append(pad + headerLine)
        }

        result.append("{")
        if (DotlinLogger.isEnabled) DotlinLogger.log("  result: ${Tools.toDisplayStringShort(result.toString())}")

        return result.toString()
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

            val elseEndPos = Tools.getElseEndPos(currentLine)
            if (DotlinLogger.isEnabled) DotlinLogger.log("elseEndPos: $elseEndPos")
            if (elseEndPos >= 0)
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

            val elseEndPos = Tools.getElseEndPos(footerLine)
            if (elseEndPos >= 0)
            {
                // no padding for "else..."
                result.append(footerLine)
                continue
            }

            if (footerLine.startsWith("{"))
            {
                // no padding for "{..."
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
