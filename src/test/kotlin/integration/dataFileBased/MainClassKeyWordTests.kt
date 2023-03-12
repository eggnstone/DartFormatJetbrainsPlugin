package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class MainClassKeyWordTests
{
    @Test
    fun normalClass()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "NormalClass.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "NormalClass.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun normalClassWithOpeningBraceAtSameLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithOpeningBraceAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithOpeningBraceAtSameLine.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun normalClassWithMixin()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithMixin.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun abstractClass()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "AbstractClass.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "AbstractClass.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithOpeningBraceAtSameLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithOpeningBraceAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithOpeningBraceAtSameLine.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun abstractClassWithMixin()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithMixin.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithMixin.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
