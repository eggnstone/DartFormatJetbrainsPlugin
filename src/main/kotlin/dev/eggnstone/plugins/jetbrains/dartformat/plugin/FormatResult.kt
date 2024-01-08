package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.ResultType

class FormatResult(val resultType: ResultType, val text: String, val throwable: Throwable? = null)
{
    companion object
    {
        fun error(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.ERROR, text = s)
        }

        fun throwable(s: String, e: Throwable): FormatResult
        {
            return FormatResult(resultType = ResultType.ERROR, text = s, throwable = e)
        }

        fun ok(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.OK, text = s)
        }

        fun warning(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.WARNING, text = s)
        }
    }
}
