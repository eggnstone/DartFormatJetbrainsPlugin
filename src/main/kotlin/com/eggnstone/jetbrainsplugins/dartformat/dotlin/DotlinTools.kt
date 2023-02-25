package com.eggnstone.jetbrainsplugins.dartformat.dotlin

class DotlinTools
{
    companion object
    {
        fun contains(s: String, c: C): Boolean
        {
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val sc = C(s.get(i).toString())
                if (sc == c)
                    return true
            }

            return false
        }

        fun replace(s: String, searchChar: C, replaceText: String): String
        {
            var result = ""
            @Suppress("ReplaceManualRangeWithIndicesCalls")
            for (i in 0 until s.length)
            {
                @Suppress("ReplaceGetOrSet")
                val sc = C(s.get(i).toString())
                if (sc == searchChar)
                    result += replaceText
                else
                    result += sc.value
            }

            return result
        }
    }
}
