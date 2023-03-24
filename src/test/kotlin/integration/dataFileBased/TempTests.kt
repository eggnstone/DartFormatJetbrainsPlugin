package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class TempTests
{
    companion object
    {
        const val inputFilePath = IntegrationConstants.dataFilesPath + "Temp.input.dart"
        const val outputFilePath = IntegrationConstants.dataFilesPath + "Temp.expected_output.dart"
    }

    @Test
    fun temp()
    {
        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
