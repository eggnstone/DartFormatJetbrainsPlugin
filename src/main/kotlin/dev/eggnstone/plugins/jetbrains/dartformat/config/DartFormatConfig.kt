package dev.eggnstone.plugins.jetbrains.dartformat.config

import kotlinx.serialization.Serializable

@Serializable
data class DartFormatConfig(
    var addNewLineAfterClosingBrace: Boolean,
    var addNewLineAfterOpeningBrace: Boolean,
    var addNewLineAfterSemicolon: Boolean,
    var addNewLineAtEndOfText: Boolean,
    var addNewLineBeforeClosingBrace: Boolean,
    var addNewLineBeforeOpeningBrace: Boolean,
    var indentationIsEnabled: Boolean,
    var indentationSpacesPerLevel: Int,
    var maxEmptyLines: Int,
    var maxEmptyLinesIsEnabled: Boolean,
    var removeTrailingCommas: Boolean,
    var version: Int? = null
)
{
    companion object
    {
        fun none(version: Int): DartFormatConfig
        {
            return DartFormatConfig(
                addNewLineAfterClosingBrace = ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE,
                addNewLineAfterOpeningBrace = ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE,
                addNewLineAfterSemicolon = ADD_NEW_LINE_AFTER_SEMICOLON_NONE,
                addNewLineAtEndOfText = ADD_NEW_LINE_AT_END_OF_TEXT_NONE,
                addNewLineBeforeClosingBrace = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE,
                addNewLineBeforeOpeningBrace = ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE,
                indentationIsEnabled = INDENTATION_IS_ENABLED_NONE,
                indentationSpacesPerLevel = INDENTATION_SPACES_PER_LEVEL_ALL,
                maxEmptyLines = MAX_EMPTY_LINES_ALL,
                maxEmptyLinesIsEnabled = MAX_EMPTY_LINES_IS_ENABLED_NONE,
                removeTrailingCommas = REMOVE_TRAILING_COMMAS_NONE,
                version = version
            )
        }

        fun default(version: Int): DartFormatConfig
        {
            return DartFormatConfig(
                addNewLineAfterClosingBrace = ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT,
                addNewLineAfterOpeningBrace = ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT,
                addNewLineAfterSemicolon = ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT,
                addNewLineAtEndOfText = ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT,
                addNewLineBeforeClosingBrace = ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT,
                addNewLineBeforeOpeningBrace = ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT,
                indentationIsEnabled = INDENTATION_IS_ENABLED_DEFAULT,
                indentationSpacesPerLevel = INDENTATION_SPACES_PER_LEVEL_ALL,
                maxEmptyLines = MAX_EMPTY_LINES_ALL,
                maxEmptyLinesIsEnabled = MAX_EMPTY_LINES_IS_ENABLED_DEFAULT,
                removeTrailingCommas = REMOVE_TRAILING_COMMAS_DEFAULT,
                version = version
            )
        }

        private const val INDENTATION_SPACES_PER_LEVEL_ALL = 4
        private const val MAX_EMPTY_LINES_ALL = 1

        private const val ADD_NEW_LINE_AFTER_CLOSING_BRACE_NONE = false
        private const val ADD_NEW_LINE_AFTER_OPENING_BRACE_NONE = false
        private const val ADD_NEW_LINE_AFTER_SEMICOLON_NONE = false
        private const val ADD_NEW_LINE_AT_END_OF_TEXT_NONE = false
        private const val ADD_NEW_LINE_BEFORE_CLOSING_BRACE_NONE = false
        private const val ADD_NEW_LINE_BEFORE_OPENING_BRACE_NONE = false
        private const val INDENTATION_IS_ENABLED_NONE = false
        private const val MAX_EMPTY_LINES_IS_ENABLED_NONE = false
        private const val REMOVE_TRAILING_COMMAS_NONE = false

        private const val ADD_NEW_LINE_AFTER_CLOSING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_AFTER_OPENING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_AFTER_SEMICOLON_DEFAULT = true
        private const val ADD_NEW_LINE_AT_END_OF_TEXT_DEFAULT = true
        private const val ADD_NEW_LINE_BEFORE_CLOSING_BRACE_DEFAULT = true
        private const val ADD_NEW_LINE_BEFORE_OPENING_BRACE_DEFAULT = true
        private const val INDENTATION_IS_ENABLED_DEFAULT = true
        private const val MAX_EMPTY_LINES_IS_ENABLED_DEFAULT = true
        private const val REMOVE_TRAILING_COMMAS_DEFAULT = true
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

        val adjustedDefaultConfig = none(version = 0)

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
        adjustedDefaultConfig.version = this.version

        return this == adjustedDefaultConfig
    }
}
