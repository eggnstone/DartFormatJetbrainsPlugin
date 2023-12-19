package dev.eggnstone.plugins.jetbrains.dartformat.config

class DartFormatConfig(isTest: Boolean = false)
{
    fun toJson(): String
    {
        return "{" +
            "\\\"addNewLineAfterClosingBrace\\\": true," +
            "\\\"addNewLineAfterOpeningBrace\\\": true," +
            "\\\"addNewLineAfterSemicolon\\\": true," +
            "\\\"addNewLineAtEndOfText\\\": true," +
            "\\\"addNewLineBeforeClosingBrace\\\": true," +
            "\\\"addNewLineBeforeOpeningBrace\\\": true," +
            "\\\"maxEmptyLines\\\": 1" +
            "}"
    }

    val isEnabled
        get(): Boolean
        {
            return removeUnnecessaryCommas
                || removeLineBreaksAfterArrows
                || indentationIsEnabled
        }

    var removeUnnecessaryCommas = isTest

    var removeLineBreaksAfterArrows = isTest

    var indentationIsEnabled = true
    var indentationSpacesPerLevel = 4
}
