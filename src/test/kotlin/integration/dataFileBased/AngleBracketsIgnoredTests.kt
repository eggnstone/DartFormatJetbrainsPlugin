package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class AngleBracketsIgnoredTests
{
    @Test
    fun wordPressTools()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "AngleBracketsIgnored.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "AngleBracketsIgnored.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
