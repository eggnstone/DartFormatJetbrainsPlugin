package dev.eggnstone.plugins.jetbrains.dartformat

import com.beust.klaxon.Json

class DartFormatException(
    @Json(name = "Type") val type: FailType,
    @Json(name = "Message") override val message: String,
    @Json(name = "Cause") override val cause: Throwable? = null
) : Exception(message, cause)