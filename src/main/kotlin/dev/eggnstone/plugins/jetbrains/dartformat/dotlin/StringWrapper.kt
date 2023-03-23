package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

class StringWrapper
{
    companion object
    {
        fun containsChar(s: String, searchChar: String): Boolean = s.contains(searchChar)

        fun containsString(s: String, searchText: String): Boolean = s.contains(searchText)

        fun endsWith(s: String, searchText: String): Boolean = s.endsWith(searchText)

        fun getSpaces(count: Int): String = " ".repeat(count)

        fun indexOf(s: String, searchText: String): Int = s.indexOf(searchText)

        fun isBlank(s: String): Boolean = s.isBlank()

        fun isEmpty(s: String) = s.isEmpty()

        fun isNotEmpty(s: String) = s.isNotEmpty()

        fun replace(s: String, searchChar: String, replaceText: String): String = s.replace(searchChar, replaceText)

        fun startsWith(s: String, searchText: String): Boolean = s.startsWith(searchText)

        fun substring(s: String, startIndex: Int): String = s.substring(startIndex)

        fun substring(s: String, startIndex: Int, endIndex: Int): String = s.substring(startIndex, endIndex)

        fun trim(s: String): String = s.trim()

        fun trimEnd(s: String): String = s.trimEnd()

        fun trimStart(s: String): String = s.trimStart()
    }
}
