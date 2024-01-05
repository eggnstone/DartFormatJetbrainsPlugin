package dev.eggnstone.plugins.jetbrains.dartformat

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class JsonTools
{
    companion object
    {
        fun parseDartFormatException(json: String): DartFormatException?
        {
            return try
            {
                val obj = Json.parseToJsonElement(json)
                val typeText = obj.jsonObject["Type"]?.jsonPrimitive?.content
                val type: FailType = if (typeText == "Warning") FailType.WARNING else FailType.ERROR
                val messageText = obj.jsonObject["Message"]?.jsonPrimitive?.content ?: ""
                val line = obj.jsonObject["Line"]?.jsonPrimitive?.content?.toInt() ?: -1
                val column = obj.jsonObject["Column"]?.jsonPrimitive?.content?.toInt() ?: -1
                DartFormatException(type, messageText, line = line, column = column)
            }
            catch (e: Exception)
            {
                DartFormatException(FailType.ERROR, "Failed to parse JSON: $json", e)
            }
        }
    }
}