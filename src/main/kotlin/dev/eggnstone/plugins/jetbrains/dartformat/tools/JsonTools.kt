package dev.eggnstone.plugins.jetbrains.dartformat.tools

import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.ExceptionSourceType
import dev.eggnstone.plugins.jetbrains.dartformat.FailType
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
                throw DartFormatException(FailType.Error, ExceptionSourceType.Local, "Failed to parse JSON/1: \"${toTransferString(json)}\"")
            }
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun toTransferString(json: String): String
        {
            return json.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r")
        }

        fun parseDartFormatException(json: String): DartFormatException
        {
            val jsonElement = parse(json)

            try
            {
                val typeText = getString(jsonElement, "Type", "Error")
                val type: FailType = if (typeText == "Warning") FailType.Warning else FailType.Error
                val messageText = getString(jsonElement, "Message", "Unknown error")
                val line = getInt(jsonElement, "Line", -1)
                val column = getInt(jsonElement, "Column", -1)
                return DartFormatException(type, ExceptionSourceType.Remote, messageText, line = line, column = column)
            }
            catch (e: Exception)
            {
                return DartFormatException(FailType.Error, ExceptionSourceType.Local, "Failed to parse JSON/2: \"${toTransferString(json)}\"")
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
