package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class Temp2Tests
{
    companion object
    {
        const val inputFilePath = IntegrationConstants.dataFilesPath + "Temp2.input.dart"
        const val outputFilePath = IntegrationConstants.dataFilesPath + "Temp2.expected_output.dart"
    }

    @Test
    fun temp2()
    {
        if (!File(inputFilePath).exists())
            return

        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun temp2Twice()
    {
        if (!File(inputFilePath).exists())
            return

        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.testTwice(inputText, expectedOutputText)
    }
}
