package integration.dataFileBased

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class CommentsTests
{
    companion object
    {
        const val inputFilePath = IntegrationConstants.dataFilesPath + "Comments.input.dart"
        const val outputFilePath = IntegrationConstants.dataFilesPath + "Comments.expected_output.dart"
        const val outputFilePath2 = IntegrationConstants.dataFilesPath + "CommentsPerfect.expected_output.dart"
    }

    @Test
    fun comments()
    {
        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.test(inputText, expectedOutputText, true)
    }

    @Test
    fun commentsTwice()
    {
        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath).readText()

        IntegrationTools.testTwice(inputText, expectedOutputText)
    }

    @Test
    fun commentsPerfect()
    {
        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath2).readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    fun commentsPerfectTwice()
    {
        val inputText = File(inputFilePath).readText()
        val expectedOutputText = File(outputFilePath2).readText()

        IntegrationTools.testTwice(inputText, expectedOutputText)
    }
}
