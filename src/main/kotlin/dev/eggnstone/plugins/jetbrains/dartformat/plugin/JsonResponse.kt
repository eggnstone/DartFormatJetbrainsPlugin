package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import dev.eggnstone.plugins.jetbrains.dartformat.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class JsonResponse(json: String, val source: String)
{
    val statusCode: Int
    val status: String

    init
    {
        try
        {
            val jsonElement = JsonTools.parse(json)
            statusCode = JsonTools.getInt(jsonElement, "StatusCode", -1)
            status = JsonTools.getString(jsonElement, "Status", "?")
        }
        catch (e: Exception)
        {
            Logger.logError("Response: Failed to parse JSON: $json")
            Logger.logError(e.toString())
            throw e
        }
    }

    companion object
    {
        fun fromErrorStream(json: String): JsonResponse
        {
            return JsonResponse(json, "ErrorStream")
        }

        fun fromOutputStream(json: String): JsonResponse
        {
            return JsonResponse(json, "OutputStream")
        }
    }
}