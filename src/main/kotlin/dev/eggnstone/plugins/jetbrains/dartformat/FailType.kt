package dev.eggnstone.plugins.jetbrains.dartformat

import com.beust.klaxon.Json

enum class FailType
{
    @Json(name = "Error")
    ERROR,

    @Json(name = "Warning")
    WARNING
}
