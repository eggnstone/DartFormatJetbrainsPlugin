package dev.eggnstone.plugins.jetbrains.dartformat.tools

import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger
{
    companion object
    {
        private var logFile: File? = null

        @Suppress("MemberVisibilityCanBePrivate")
        var isEnabled: Boolean = true

        fun logDebug(s: String)
        {
            if (!isEnabled)
                return

            println("DEBUG: $s")
            logToFile("DEBUG: $s")
        }

        fun logInfo(s: String)
        {
            if (!isEnabled)
                return

            println("INFO:  $s")
            logToFile("INFO:  $s")
        }

        fun logWarning(s: String)
        {
            if (!isEnabled)
                return

            println("WARN:  $s")
            logToFile("WARN:  $s")
        }

        fun logError(s: String)
        {
            if (!isEnabled)
                return

            println("ERROR: $s")
            logToFile("ERROR: $s")
        }

        private fun logToFile(s: String)
        {
            if (logFile == null)
                if (!createLogFile())
                    return

            val dateTimeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val timestamp = dateTimeFormatter.format(System.currentTimeMillis())

            logFile!!.appendText("$timestamp $s\n")
        }

        private fun createLogFile(): Boolean
        {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            val timestamp = formatter.format(now)

            val pid = ProcessHandle.current().pid()

            val logFileName = "DartFormatPlugin_${timestamp}_$pid.log"
            val logFilePath = OsTools.getTempDirName() + "/" + logFileName

            try
            {
                logFile = File(logFilePath)
                logFile!!.createNewFile()
                return true
            }
            catch (e: Exception)
            {
                logFile = null
                println("ERROR: logFilePath: $logFilePath")
                println("$e")
                return false
            }
        }
    }
}
