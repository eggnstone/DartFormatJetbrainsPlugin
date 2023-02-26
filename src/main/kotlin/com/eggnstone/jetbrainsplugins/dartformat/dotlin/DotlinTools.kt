package com.eggnstone.jetbrainsplugins.dartformat.dotlin

class DotlinTools
{
    companion object
    {
        fun contains(s: String, c: DotlinChar): Boolean
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val sc = DotlinChar(s.get(i).toString())
                if (sc == c)
                    return true
            }

            return false
        }

        fun replace(s: String, searchChar: DotlinChar, replaceText: String): String
        {
            var result = ""
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val sc = DotlinChar(s.get(i).toString())
                if (sc == searchChar)
                    result += replaceText
                else
                    result += sc.value
            }

            return result
        }
    }
}
