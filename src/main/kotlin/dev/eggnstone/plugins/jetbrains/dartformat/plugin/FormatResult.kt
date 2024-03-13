package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.enums.ResultType

class FormatResult(val resultType: ResultType, val text: String, val throwable: Throwable? = null)
{
    companion object
    {
        fun error(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.Error, text = s)
        }

        fun throwable(s: String, e: Throwable): FormatResult
        {
            return FormatResult(resultType = ResultType.Error, text = s, throwable = e)
        }

        fun ok(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.Ok, text = s)
        }

        fun warning(s: String): FormatResult
        {
            return FormatResult(resultType = ResultType.Warning, text = s)
        }
    }
}
