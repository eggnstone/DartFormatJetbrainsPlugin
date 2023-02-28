package com.eggnstone.jetbrainsplugins.dartformat.integration

import integration.IntegrationConstants
import integration.IntegrationTools
import org.junit.Test
import java.io.File

class AngleBracketsIgnoredTests
{
    @Test
    fun wordPressTools()
    {
        val inputText = File(IntegrationConstants.testDataPath + "AngleBracketsIgnored.input.dart").readText()
        val expectedOutputText = File(IntegrationConstants.testDataPath + "AngleBracketsIgnored.expected_output.dart").readText()

        IntegrationTools.test(inputText, expectedOutputText)
    }
}
