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
            "\"IndentationSpacesPerLevel\": " + finalIndentationSpacesPerLevel + "," +
            "\"MaxEmptyLines\": " + finalMaxEmptyLines + "," +
            "\"RemoveTrailingCommas\": " + removeTrailingCommas +
            "}"
    }

    fun hasNothingEnabled(): Boolean
    {
        //if (Constants.LOG_VERBOSE) Logger.logVerbose("DartFormatConfig.hasNothingEnabled()")

        val adjustedDefaultConfig = DartFormatConfigV1()

        /*
        Logger.log("  addedNewLineAfterClosingBrace: ${this.addNewLineAfterClosingBrace} == ${adjustedDefaultConfig.addNewLineAfterClosingBrace}")
        Logger.log("  addNewLineAfterOpeningBrace: ${this.addNewLineAfterOpeningBrace} == ${adjustedDefaultConfig.addNewLineAfterOpeningBrace}")
        Logger.log("  addNewLineAfterSemicolon: ${this.addNewLineAfterSemicolon} == ${adjustedDefaultConfig.addNewLineAfterSemicolon}")
        Logger.log("  addNewLineAtEndOfText: ${this.addNewLineAtEndOfText} == ${adjustedDefaultConfig.addNewLineAtEndOfText}")
        Logger.log("  addNewLineBeforeClosingBrace: ${this.addNewLineBeforeClosingBrace} == ${adjustedDefaultConfig.addNewLineBeforeClosingBrace}")
        Logger.log("  addNewLineBeforeOpeningBrace: ${this.addNewLineBeforeOpeningBrace} == ${adjustedDefaultConfig.addNewLineBeforeOpeningBrace}")
        Logger.log("  indentationIsEnabled: ${this.indentationIsEnabled} == ${adjustedDefaultConfig.indentationIsEnabled}")
        Logger.log("  indentationSpacesPerLevel: ${this.indentationSpacesPerLevel} == ${adjustedDefaultConfig.indentationSpacesPerLevel}")
        Logger.log("  maxEmptyLines: ${this.maxEmptyLines} == ${adjustedDefaultConfig.maxEmptyLines}")
        Logger.log("  maxEmptyLinesIsEnabled: ${this.maxEmptyLinesIsEnabled} == ${adjustedDefaultConfig.maxEmptyLinesIsEnabled}")
        Logger.log("  removeTrailingCommas: ${this.removeTrailingCommas} == ${adjustedDefaultConfig.removeTrailingCommas}")
        */

        adjustedDefaultConfig.indentationSpacesPerLevel = this.indentationSpacesPerLevel
        adjustedDefaultConfig.maxEmptyLines = this.maxEmptyLines

        return this == adjustedDefaultConfig
    }
}
