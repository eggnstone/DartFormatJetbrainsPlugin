package dev.eggnstone.plugins.jetbrains.dartformat.config

class DartFormatConfig(
    /*var addNewLineAfterClosingBrace: Boolean,
    var addNewLineAfterOpeningBrace: Boolean,
    var addNewLineAfterSemicolon: Boolean,
    var addNewLineAtEndOfText: Boolean,
    var addNewLineBeforeClosingBrace: Boolean,
    var addNewLineBeforeOpeningBrace: Boolean,
    var maxEmptyLines: Int,
    var maxEmptyLinesIsEnabled: Boolean,
    var indentationIsEnabled: Boolean,
    var indentationSpacesPerLevel: Int,
    var removeUnnecessaryCommas: Boolean,
    var removeLineBreaksAfterArrows: Boolean*/
)
{
    companion object
    {
        /*private const val ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT = true
        private const val ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT = true
        private const val ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT = true
        private const val INDENTATION_IS_ENABLED_DEFAULT = true
        private const val INDENTATION_SPACES_PER_LEVEL_DEFAULT = 4
        private const val MAX_EMPTY_LINES_DEFAULT = 1
        private const val MAX_EMPTY_LINES_IS_ENABLED_DEFAULT = true*/
        /*private const val REMOVE_LINE_BREAKS_AFTER_ARROWS_DEFAULT = true
        private const val REMOVE_UNNECESSARY_COMMAS_DEFAULT = true*/

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
        /*private const val REMOVE_LINE_BREAKS_AFTER_ARROWS_NONE = false
        private const val REMOVE_UNNECESSARY_COMMAS_NONE = false*/

        /*fun createDefault(): DartFormatConfig
        {
            val c = DartFormatConfig()
            c.addNewLineAfterClosingBrace = ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT;
            addNewLineAfterOpeningBrace = ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT,
            addNewLineAfterSemicolon = ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT,
            addNewLineAtEndOfText = ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT,
            addNewLineBeforeClosingBrace = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT,
            addNewLineBeforeOpeningBrace = ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT,
            indentationIsEnabled = INDENTATION_IS_ENABLED_DEFAULT,
            indentationSpacesPerLevel = INDENTATION_SPACES_PER_LEVEL_DEFAULT,
            maxEmptyLines = MAX_EMPTY_LINES_DEFAULT,
            maxEmptyLinesIsEnabled = MAX_EMPTY_LINES_IS_ENABLED_DEFAULT,
            removeLineBreaksAfterArrows = REMOVE_LINE_BREAKS_AFTER_ARROWS_DEFAULT,
            removeUnnecessaryCommas = REMOVE_UNNECESSARY_COMMAS_DEFAULT
            )
        }

        fun createNone(): DartFormatConfig
        {
            return DartFormatConfig(
                addNewLineAfterClosingBrace = ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE,
                addNewLineAfterOpeningBrace = ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE,
                addNewLineAfterSemicolon = ADD_NEW_LINE_AFTER_SEMICOLON_NONE,
                addNewLineAtEndOfText = ADD_NEW_LINE_AT_END_OF_TEXT_NONE,
                addNewLineBeforeClosingBrace = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE,
                addNewLineBeforeOpeningBrace = ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE,
                indentationIsEnabled = INDENTATION_IS_ENABLED_NONE,
                indentationSpacesPerLevel = INDENTATION_SPACES_PER_LEVEL_NONE,
                maxEmptyLines = MAX_EMPTY_LINES_NONE,
                maxEmptyLinesIsEnabled = MAX_EMPTY_LINES_IS_ENABLED_NONE,
                removeLineBreaksAfterArrows = REMOVE_LINE_BREAKS_AFTER_ARROWS_NONE,
                removeUnnecessaryCommas = REMOVE_UNNECESSARY_COMMAS_NONE
            )
        }*/
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
    /*var removeUnnecessaryCommas = REMOVE_UNNECESSARY_COMMAS_NONE
    var removeLineBreaksAfterArrows = REMOVE_LINE_BREAKS_AFTER_ARROWS_NONE*/

    fun toJson(): String
    {
        val finalIndentationSpacesPerLevel = if (indentationIsEnabled) indentationSpacesPerLevel else -1
        val finalMaxEmptyLines = if (maxEmptyLinesIsEnabled) maxEmptyLines else -1

        return "{" +
            "\\\"addNewLineAfterClosingBrace\\\": " + addNewLineAfterClosingBrace + "," +
            "\\\"addNewLineAfterOpeningBrace\\\": " + addNewLineAfterOpeningBrace + "," +
            "\\\"addNewLineAfterSemicolon\\\": " + addNewLineAfterSemicolon + "," +
            "\\\"addNewLineAtEndOfText\\\": " + addNewLineAtEndOfText + "," +
            "\\\"addNewLineBeforeClosingBrace\\\": " + addNewLineBeforeClosingBrace + "," +
            "\\\"addNewLineBeforeOpeningBrace\\\": " + addNewLineBeforeOpeningBrace + "," +
            "\\\"indentationSpacesPerLevel\\\": " + finalIndentationSpacesPerLevel + "," +
            "\\\"maxEmptyLines\\\": " + finalMaxEmptyLines +
            //"," +
            /*"\\\"removeLineBreaksAfterArrows\\\": " + removeLineBreaksAfterArrows + "," +
            "\\\"removeUnnecessaryCommas\\\": " + removeUnnecessaryCommas +*/
            "}"
    }
}
