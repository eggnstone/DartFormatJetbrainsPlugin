package dev.eggnstone.plugins.jetbrains.dartformat.tools

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Logger
{
    companion object
    {
        private const val LOG_FILE_RETENTION_IN_DAYS: Long = 30
        private const val MAX_LOG_FILE_SIZE_IN_BYTES: Long = 10L * 1024L * 1024L

        // Computed at class load. Direct System.getProperty (not OsTools) to avoid a circular
        // companion-init: OsTools.Companion.<init> creates OsTools.instance, whose init {} calls Logger.logDebug.
        val logFilePath: String = computeLogFilePath()
        private val timestampFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        private var logFile: File? = null
        private var logFileSizeBytes: Long = 0L

        @Suppress("MemberVisibilityCanBePrivate")
        var isEnabled: Boolean = true

        init
        {
            cleanupOldLogFiles()
        }

        fun logVerbose(s: String)
        {
            if (!isEnabled)
                return

            println("VERB:  $s")
            logToFile("VERB:  $s")
        }

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
            val file = logFile ?: createLogFile() ?: return
            val timestamp = timestampFormatter.format(LocalDateTime.now())
            val line = "$timestamp $s\n"
            file.appendText(line)
            logFileSizeBytes += line.toByteArray(Charsets.UTF_8).size.toLong()

            if (logFileSizeBytes > MAX_LOG_FILE_SIZE_IN_BYTES)
                rotateLogFile()
        }

        private fun createLogFile(): File?
        {
            return try
            {
                val newFile = File(logFilePath)
                newFile.createNewFile()
                logFile = newFile
                logFileSizeBytes = newFile.length()
                newFile
            }
            catch (e: Exception)
            {
                println("ERROR: logFilePath: $logFilePath")
                println("$e")
                null
            }
        }

        // Mirrors dart_format 2.2.0's _rotateLogFile: caps each session's log at roughly
        // 2 * MAX_LOG_FILE_SIZE_IN_BYTES — the current file plus one .old backup. The current
        // path stays the same so the settings-page diagnostics link and error-notification
        // body still point at recent entries; older entries are in <path>.old.
        private fun rotateLogFile()
        {
            val currentPath = logFilePath
            logFile = null
            logFileSizeBytes = 0L

            try
            {
                val oldFile = File("$currentPath.old")
                if (oldFile.exists())
                    oldFile.delete()
                File(currentPath).renameTo(oldFile)
            }
            catch (e: Exception)
            {
                // Best-effort — next write will reopen / recreate currentPath via createLogFile().
            }
        }

        private fun computeLogFilePath(): String
        {
            val now = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
            val timestamp = formatter.format(now)
            val pid = ProcessHandle.current().pid()
            val tempDir = System.getProperty("java.io.tmpdir")
            return File(tempDir, "DartFormatPlugin_${timestamp}_$pid.log").path
        }

        // Mirrors dart_format 2.2.0's _cleanupOldLogFiles: drops any DartFormatPlugin_*.log or
        // DartFormatPlugin_*.log.old file in java.io.tmpdir that hasn't been touched in
        // LOG_FILE_RETENTION_IN_DAYS days. Best-effort: per-file failures (locked by a concurrent
        // IDE instance on Windows, permission errors) are swallowed via File.delete()'s false-return
        // contract. Never deletes the current PID's .log or its rotated .log.old sibling.
        private fun cleanupOldLogFiles()
        {
            try
            {
                val tempDir = File(System.getProperty("java.io.tmpdir"))
                val cutoffMillis = System.currentTimeMillis() - LOG_FILE_RETENTION_IN_DAYS * 24L * 60L * 60L * 1000L
                val currentPidSuffix = "_${ProcessHandle.current().pid()}.log"
                val currentPidOldSuffix = "$currentPidSuffix.old"

                val files = tempDir.listFiles() ?: return
                for (file in files)
                {
                    if (!file.isFile)
                        continue

                    val name = file.name
                    if (!name.startsWith("DartFormatPlugin_"))
                        continue

                    if (!name.endsWith(".log") && !name.endsWith(".log.old"))
                        continue

                    if (name.endsWith(currentPidSuffix) || name.endsWith(currentPidOldSuffix))
                        continue

                    if (file.lastModified() < cutoffMillis)
                        file.delete()
                }
            }
            catch (e: Exception)
            {
                // tmpdir not listable / unexpected I/O — skip cleanup, not fatal.
            }
        }
    }
}
