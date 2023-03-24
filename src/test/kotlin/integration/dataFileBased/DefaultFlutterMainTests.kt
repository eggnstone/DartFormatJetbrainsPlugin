package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DefaultFlutterMainTests
{
    companion object
    {
        const val inputIndentationRemoved = IntegrationConstants.dataFilesPath + "default_flutter_main_indentation_removed.input.dart"
        const val inputIndentationRemovedSpacesRemoved = IntegrationConstants.dataFilesPath + "default_flutter_main_indentation_removed_spaces_removed.input.dart"
        const val outputIndentationRestored = IntegrationConstants.dataFilesPath + "default_flutter_main_indentation_restored.expected_output.dart"
        const val outputIndentationRestoredCommasRemoved = IntegrationConstants.dataFilesPath + "default_flutter_main_indentation_restored_commas_removed.expected_output.dart"
        const val outputIndentationRestoredLineBreaksAdded = IntegrationConstants.dataFilesPath + "default_flutter_main_indentation_restored_linebreaks_added.expected_output.dart"
    }

    @Test
    fun defaultFlutterMain_IndentationRemoved_IndentationRestored()
    {
        val inputText = File(inputIndentationRemoved).readText()
        val expectedOutputText = File(outputIndentationRestored).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun defaultFlutterMain_IndentationRemoved_IndentationRestored_Twice()
    {
        val inputText = File(inputIndentationRemoved).readText()
        val expectedOutputText = File(outputIndentationRestored).readText()

        IntegrationTools.testTwice(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain_IndentationRemoved_IndentationRestoredCommasRemoved()
    {
        val inputText = File(inputIndentationRemoved).readText()
        val expectedOutputText = File(outputIndentationRestoredCommasRemoved).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain_IndentationRemoved_IndentationRestoredLineBreaksAdded()
    {
        val inputText = File(inputIndentationRemoved).readText()
        val expectedOutputText = File(outputIndentationRestoredLineBreaksAdded).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain_IndentationRemovedSpacesRemoved_IndentationRestored()
    {
        val inputText = File(inputIndentationRemovedSpacesRemoved).readText()
        val expectedOutputText = File(outputIndentationRestored).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain_IndentationRemovedSpacesRemoved_IndentationRestoredLineBreaksAdded()
    {
        val inputText = File(inputIndentationRemovedSpacesRemoved).readText()
        val expectedOutputText = File(outputIndentationRestoredLineBreaksAdded).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
