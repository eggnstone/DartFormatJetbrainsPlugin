package dev.eggnstone.plugins.jetbrains.dartformat.config

class DartFormatConfig
{
    companion object
    {
        private const val ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE = false
        private const val ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE = false
        private const val ADD_NEW_LINE_AFTER_SEMICOLON_NONE = false
        private const val ADD_NEW_LINE_AT_END_OF_TEXT_NONE = false
        private const val ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE = false
        private const val ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE = false
        private const val INDENTATION_IS_ENABLED_NONE = false
        private const val INDENTATION_SPACES_PER_LEVEL_NONE = 4
        private const val MAX_EMPTY_LINES_IS_ENABLED_NONE = false
        private const val MAX_EMPTY_LINES_NONE = 1
    }

    var addNewLineAfterClosingBrace = ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE
    var addNewLineAfterOpeningBrace = ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE
    var addNewLineAfterSemicolon = ADD_NEW_LINE_AFTER_SEMICOLON_NONE
    var addNewLineAtEndOfText = ADD_NEW_LINE_AT_END_OF_TEXT_NONE
    var addNewLineBeforeClosingBrace = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE
    var addNewLineBeforeOpeningBrace = ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE
    var maxEmptyLines = MAX_EMPTY_LINES_NONE
    var maxEmptyLinesIsEnabled = MAX_EMPTY_LINES_IS_ENABLED_NONE
    var indentationIsEnabled = INDENTATION_IS_ENABLED_NONE
    var indentationSpacesPerLevel = INDENTATION_SPACES_PER_LEVEL_NONE

    fun toJson(): String
    {
        val finalIndentationSpacesPerLevel = if (indentationIsEnabled) indentationSpacesPerLevel else -1
        val finalMaxEmptyLines = if (maxEmptyLinesIsEnabled) maxEmptyLines else -1

        return "{" +
        "\\\"AddNewLineAfterClosingBrace\\\": " + addNewLineAfterClosingBrace + "," +
        "\\\"AddNewLineAfterOpeningBrace\\\": " + addNewLineAfterOpeningBrace + "," +
        "\\\"AddNewLineAfterSemicolon\\\": " + addNewLineAfterSemicolon + "," +
        "\\\"AddNewLineAtEndOfText\\\": " + addNewLineAtEndOfText + "," +
        "\\\"AddNewLineBeforeClosingBrace\\\": " + addNewLineBeforeClosingBrace + "," +
        "\\\"AddNewLineBeforeOpeningBrace\\\": " + addNewLineBeforeOpeningBrace + "," +
        "\\\"IndentationSpacesPerLevel\\\": " + finalIndentationSpacesPerLevel + "," +
        "\\\"MaxEmptyLines\\\": " + finalMaxEmptyLines +
        "}"
    }
}
