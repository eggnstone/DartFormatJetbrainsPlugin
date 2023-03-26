package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class Temp1Tests
{
    companion object
    {
        const val inputFilePath = IntegrationConstants.dataFilesPath + "Temp1.input.dart"
        const val outputFilePath = IntegrationConstants.dataFilesPath + "Temp1.expected_output.dart"
    }

    @Test
    fun temp1()
    {
        if (!File(inputFilePath).exists())
            return

        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun temp1Twice()
    {
        if (!File(inputFilePath).exists())
            return

        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.testTwice(inputText, expectedOutputText)
    }
}
