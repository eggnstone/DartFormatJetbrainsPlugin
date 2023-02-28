package com.eggnstone.jetbrainsplugins.dartformat.integration

import integration.IntegrationConstants
import org.junit.Ignore
import org.junit.Test
import java.io.File

class DefaultFlutterMainTests
{
    @Test
    fun defaultFlutterMain()
    {
        val inputText = File(IntegrationConstants.testDataPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "default_flutter_main.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }

    @Test
    @Ignore
    fun defaultFlutterMain2()
    {
        val inputText = File(IntegrationConstants.testDataPath + "default_flutter_main.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "default_flutter_main2.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
