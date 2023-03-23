package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

class StringWrapper
{
    companion object
    {
        fun containsChar(s: String, searchChar: String): Boolean
        {
            return s.contains(searchChar)
        }

        fun containsString(s: String, searchText: String): Boolean
        {
            return s.contains(searchText)
        }

        fun substring(s: String, startIndex: Int): String
        {
            return s.substring(startIndex)
        }

        fun substring(s: String, startIndex: Int, endIndex: Int): String
        {
            return s.substring(startIndex, endIndex)
        }
    }
}
