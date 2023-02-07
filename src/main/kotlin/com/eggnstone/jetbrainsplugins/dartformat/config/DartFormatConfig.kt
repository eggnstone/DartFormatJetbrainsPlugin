package com.eggnstone.jetbrainsplugins.dartformat.config

class DartFormatConfig(isTest: Boolean = false)
{
    var removeUnnecessaryCommas = isTest
    var removeUnnecessaryLineBreaksAfterArrows = isTest
}
