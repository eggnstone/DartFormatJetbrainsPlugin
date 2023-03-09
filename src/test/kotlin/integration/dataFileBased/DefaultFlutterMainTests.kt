package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DefaultFlutterMainTests
{
    @Test
    fun defaultFlutterMain()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "default_flutter_main.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun defaultFlutterMainSimplifiedLineBreaks()
    {
        val inputTextRaw = File(IntegrationConstants.dataFilesPath + "default_flutter_main.input.dart").readText()
        val inputText = inputTextRaw.replace("\r\n", "\n")
        val expectedOutputTextRaw = File(IntegrationConstants.dataFilesPath + "default_flutter_main.expected_output.dart").readText()
        val expectedOutputText = expectedOutputTextRaw.replace("\r\n", "\n")

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain2()
    {
        val inputText = File(IntegrationConstants.dataFilesPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.dataFilesPath + "default_flutter_main2.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
