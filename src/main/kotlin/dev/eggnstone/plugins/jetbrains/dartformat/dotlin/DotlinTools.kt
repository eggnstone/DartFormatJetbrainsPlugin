package dev.eggnstone.plugins.jetbrains.dartformat.dotlin

class DotlinTools
{
    // .add() should be replaced with +=

    companion object
    {
        fun <T> clone(inputList: List<T>): MutableList<T>
        {
            val outputList = mutableListOf<T>()
            outputList.addAll(inputList)
            return outputList
        }

        // List<T>.isEmpty
        @Suppress("ReplaceSizeZeroCheckWithIsEmpty")
        fun <T> isEmpty(l: List<T>): Boolean = l.size == 0

        // List<T>.isEmpty
        @Suppress("ReplaceSizeCheckWithIsNotEmpty")
        fun <T> isNotEmpty(l: List<T>): Boolean = l.size > 0

        fun last(list: List<String>): String = list[list.size - 1]

        // kotlin minOf
        @Suppress("MemberVisibilityCanBePrivate")
        fun minOf(a: Int, b: Int): Int = if (a < b) a else b
    }
}
