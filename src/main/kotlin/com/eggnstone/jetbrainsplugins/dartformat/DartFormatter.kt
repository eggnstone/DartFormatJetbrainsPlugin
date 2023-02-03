package com.eggnstone.jetbrainsplugins.dartformat

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DartFormatter
{
    companion object
    {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")

        fun format(input: String): String
        {
            val output = StringBuilder()

            //output.append("// Formatted at ${dateTimeFormatter.withZone(ZoneOffset.UTC).format(Instant.now())}\n")
            output.append(input)

            return output.toString()
        }
    }
}
