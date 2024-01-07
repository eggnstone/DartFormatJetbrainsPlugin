package dev.eggnstone.plugins.jetbrains.dartformat.json

import dev.eggnstone.plugins.jetbrains.dartformat.tools.JsonTools
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.StringTools

class JsonResponse(json: String, private val source: String)
{
    companion object
    {
        fun fromErrorStream(json: String): JsonResponse
        {
            return JsonResponse(json, "ErrorStream")
        }

        fun fromInputStream(json: String): JsonResponse
        {
            return JsonResponse(json, "OutputStream")
        }
    }

    val statusCode: Int
    val status: String
    val contentLength: Int?

    init
    {
        try
        {
            Logger.log("JsonResponse: ${StringTools.toDisplayString(json)}")
            val jsonElement = JsonTools.parse(json)
            statusCode = JsonTools.getInt(jsonElement, "StatusCode", -1)
            status = JsonTools.getString(jsonElement, "Status", "?")
            contentLength = JsonTools.getInt(jsonElement, "ContentLength", -1)
        }
        catch (e: Exception)
        {
            Logger.logError(e.toString())
            Logger.logError("JsonResponse: Failed to parse JSON/3: ${StringTools.toDisplayString(json)}")
            throw e
        }
    }

    override fun toString(): String
    {
        return "JsonResponse(statusCode=$statusCode, status='$status', source='$source')"
    }
}