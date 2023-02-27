package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

class DotlinTools
{
    companion object
    {
        fun contains(s: String, searchChar: String): Boolean
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val originalChar = s.get(i).toString()
                if (originalChar == searchChar)
                    return true
            }

            return false
        }

        fun replace(s: String, searchChar: String, replaceText: String): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val originalChar = s.get(i).toString()
                result += if (originalChar == searchChar) replaceText else originalChar
            }

            return result
        }

        /*fun substring(s:String, startIndex:Int, endIndex:Int): String
        {
            var result = ""

            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in startIndex until minOf(s.length, endIndex))
            {
                @Suppress("ReplaceGetOrSet")
                val originalChar = s.get(i).toString()
                result += originalChar
            }

            return result
        }*/
    }
}
