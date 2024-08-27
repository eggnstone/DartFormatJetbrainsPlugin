package dev.eggnstone.plugins.jetbrains.dartformat.config

data class DartFormatConfigV1(
    var addNewLineAfterClosingBrace: Boolean = ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE,
    var addNewLineAfterOpeningBrace: Boolean = ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE,
    var addNewLineAfterSemicolon: Boolean = ADD_NEW_LINE_AFTER_SEMICOLON_NONE,
    var addNewLineAtEndOfText: Boolean = ADD_NEW_LINE_AT_END_OF_TEXT_NONE,
    var addNewLineBeforeClosingBrace: Boolean = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE,
    var addNewLineBeforeOpeningBrace: Boolean = ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE,
    var indentationIsEnabled: Boolean = INDENTATION_IS_ENABLED_NONE,
    var indentationSpacesPerLevel: Int = INDENTATION_SPACES_PER_LEVEL_NONE,
    var maxEmptyLines: Int = MAX_EMPTY_LINES_NONE,
    var maxEmptyLinesIsEnabled: Boolean = MAX_EMPTY_LINES_IS_ENABLED_NONE,
    var removeTrailingCommas: Boolean = REMOVE_TRAILING_COMMAS_NONE
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
        private const val REMOVE_TRAILING_COMMAS_NONE = false
    }
}
