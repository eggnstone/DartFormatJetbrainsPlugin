package dev.eggnstone.plugins.jetbrains.dartformat.config

class DartFormatConfig(isTest: Boolean = false)
{
    val isEnabled
        get(): Boolean
        {
            return removeUnnecessaryCommas
            || removeLineBreaksAfterArrows
            || indentationIsEnabled
        }

    var removeUnnecessaryCommas = isTest

    var removeLineBreaksAfterArrows = isTest

    var indentationIsEnabled = isTest
    var indentationSpacesPerLevel = 4
}
