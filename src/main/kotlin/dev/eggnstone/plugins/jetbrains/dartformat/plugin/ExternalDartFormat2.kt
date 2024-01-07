package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.StreamReader
import java.io.BufferedWriter

class ExternalDartFormat2(inputReader: StreamReader, errorReader: StreamReader, outputWriter: BufferedWriter, process: Process, project: Project)
{
    fun format(inputText: String): FormatResult
    {
        try
        {
            return FormatResult.warning("TODO")
        }
        catch (e: Exception)
        {
            return FormatResult.error(e.message ?: "Unknown error")
        }
        /*catch (e: Error)
        {
            return FormatResult.error(e.message ?: "Unknown error")
        }*/
    }
}
