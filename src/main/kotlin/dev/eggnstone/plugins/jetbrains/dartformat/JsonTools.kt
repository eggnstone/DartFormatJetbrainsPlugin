package dev.eggnstone.plugins.jetbrains.dartformat

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class JsonTools
{
    companion object
    {
        fun parse(json: String): JsonElement
        {
            try
            {
                return Json.parseToJsonElement(json)
            }
            catch (e: Exception)
            {
                throw DartFormatException(FailType.ERROR, "Failed to parse JSON: $json", e)
            }
        }

        fun parseDartFormatException(json: String): DartFormatException?
        {
            val jsonElement = parse(json)

            try
            {
                val typeText = getString(jsonElement, "Type", "Error")
                val type: FailType = if (typeText == "Warning") FailType.WARNING else FailType.ERROR
                val messageText = getString(jsonElement, "Message", "Unknown error")
                val line = getInt(jsonElement, "Line", -1)
                val column = getInt(jsonElement, "Column", -1)
                return DartFormatException(type, messageText, line = line, column = column)
            }
            catch (e: Exception)
            {
                return DartFormatException(FailType.ERROR, "Failed to parse JSON: $json", e)
            }
        }

        fun getString(jsonElement: JsonElement, key: String, default: String): String
        {
            return jsonElement.jsonObject[key]?.jsonPrimitive?.content ?: default
        }

        fun getInt(jsonElement: JsonElement, key: String, default: Int): Int
        {
            return jsonElement.jsonObject[key]?.jsonPrimitive?.content?.toInt() ?: default
        }
    }
}
