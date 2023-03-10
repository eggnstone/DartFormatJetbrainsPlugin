package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class SetStateTests
{
    @Test
    fun setState_singleLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "setState_singleLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "setState_singleLine.expected_output.dart").readText()
        // TODO

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun setStateWithArrow_singleLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "setStateWithArrow_singleLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "setStateWithArrow_singleLine.expected_output.dart").readText()
        // TODO

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun setState_multipleLines()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "setState_multipleLines.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "setState_multipleLines.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun setStateWithArrow_multipleLines()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "setStateWithArrow_multipleLines.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "setStateWithArrow_multipleLines.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
