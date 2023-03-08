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
    fun normalClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "NormalClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

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
    fun abstractClassWithOpeningCurlyBracketAtSameLine()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "AbstractClassWithOpeningCurlyBracketAtSameLine.expected_output.dart").readText()

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
