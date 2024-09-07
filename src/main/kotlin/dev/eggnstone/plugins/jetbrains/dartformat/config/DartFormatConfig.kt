package dev.eggnstone.plugins.jetbrains.dartformat.config

data class DartFormatConfig(
    var addNewLineAfterClosingBrace: Boolean = ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT,
    var addNewLineAfterOpeningBrace: Boolean = ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT,
    var addNewLineAfterSemicolon: Boolean = ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT,
    var addNewLineAtEndOfText: Boolean = ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT,
    var addNewLineBeforeClosingBrace: Boolean = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT,
    var addNewLineBeforeOpeningBrace: Boolean = ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT,
    var fixSpaces: Boolean = FIX_SPACES_DEFAULT,
    var indentationIsEnabled: Boolean = INDENTATION_IS_ENABLED_DEFAULT,
    var indentationSpacesPerLevel: Int = INDENTATION_SPACES_PER_LEVEL_DEFAULT,
    var maxEmptyLines: Int = MAX_EMPTY_LINES_DEFAULT,
    var maxEmptyLinesIsEnabled: Boolean = MAX_EMPTY_LINES_IS_ENABLED_DEFAULT,
    var removeTrailingCommas: Boolean = REMOVE_TRAILING_COMMAS_DEFAULT,
    //
    var currentVersionText: String? = null,
    var latestVersionText: String? = null,
    var majorVersion: Int? = null,
    var minorVersion: Int? = null,
    var patchVersion: Int? = null
)
{
    private var _loaded: Boolean? = null

    fun getLoadedTransient(): Boolean? = _loaded

    fun setLoaded(b: Boolean?)
    {
        _loaded = b
    }

    companion object
    {
        fun current(): DartFormatConfig
        {
            return DartFormatConfig(majorVersion = CURRENT_MAJOR_VERSION, minorVersion = CURRENT_MINOR_VERSION, patchVersion = CURRENT_PATCH_VERSION)
        }

        const val CURRENT_MAJOR_VERSION: Int = 2
        const val CURRENT_MINOR_VERSION: Int = 1
        const val CURRENT_PATCH_VERSION: Int = 0

        private const val ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT = false
        private const val ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT = false
        private const val ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT = false
        private const val ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT = false
        private const val ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT = false
        private const val ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT = false
        private const val FIX_SPACES_DEFAULT = false
        private const val INDENTATION_IS_ENABLED_DEFAULT = false
        private const val MAX_EMPTY_LINES_IS_ENABLED_DEFAULT = false
        private const val REMOVE_TRAILING_COMMAS_DEFAULT = false

        private const val INDENTATION_SPACES_PER_LEVEL_DEFAULT = 4
        private const val MAX_EMPTY_LINES_DEFAULT = 1
    }

    fun toJson(): String
    {
        val finalIndentationSpacesPerLevel = if (indentationIsEnabled) indentationSpacesPerLevel else -1
        val finalMaxEmptyLines = if (maxEmptyLinesIsEnabled) maxEmptyLines else -1

        return "{" +
            "\"AddNewLineAfterClosingBrace\": " + addNewLineAfterClosingBrace + "," +
            "\"AddNewLineAfterOpeningBrace\": " + addNewLineAfterOpeningBrace + "," +
            "\"AddNewLineAfterSemicolon\": " + addNewLineAfterSemicolon + "," +
            "\"AddNewLineAtEndOfText\": " + addNewLineAtEndOfText + "," +
            "\"AddNewLineBeforeClosingBrace\": " + addNewLineBeforeClosingBrace + "," +
            "\"AddNewLineBeforeOpeningBrace\": " + addNewLineBeforeOpeningBrace + "," +
            "\"FixSpaces\": " + fixSpaces + "," +
            "\"IndentationSpacesPerLevel\": " + finalIndentationSpacesPerLevel + "," +
            "\"MaxEmptyLines\": " + finalMaxEmptyLines + "," +
            "\"RemoveTrailingCommas\": " + removeTrailingCommas +
            "}"
    }

    fun hasNothingEnabled(): Boolean
    {
        return !addNewLineAfterClosingBrace &&
            !addNewLineAfterOpeningBrace &&
            !addNewLineAfterSemicolon &&
            !addNewLineAtEndOfText &&
            !addNewLineBeforeClosingBrace &&
            !addNewLineBeforeOpeningBrace &&
            !fixSpaces &&
            !indentationIsEnabled &&
            !maxEmptyLinesIsEnabled &&
            !removeTrailingCommas
    }
}
