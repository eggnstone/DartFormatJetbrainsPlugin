package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class DartFormatConfigUpdater
{
    companion object
    {
        fun updateV1toV2(oldConfig: DartFormatConfig): DartFormatConfig
        {
            if (Constants.DEBUG_CONFIG) Logger.logDebug("DartFormatConfigUpdater.updateV1toV2: updating V1 to V2")
            return DartFormatConfig(
                addNewLineAfterClosingBrace = oldConfig.addNewLineAfterClosingBrace,
                addNewLineAfterOpeningBrace = oldConfig.addNewLineAfterOpeningBrace,
                addNewLineAfterSemicolon = oldConfig.addNewLineAfterSemicolon,
                addNewLineAtEndOfText = oldConfig.addNewLineAtEndOfText,
                addNewLineBeforeClosingBrace = oldConfig.addNewLineBeforeClosingBrace,
                addNewLineBeforeOpeningBrace = oldConfig.addNewLineBeforeOpeningBrace,
                indentationIsEnabled = oldConfig.indentationIsEnabled,
                indentationSpacesPerLevel = oldConfig.indentationSpacesPerLevel,
                maxEmptyLines = oldConfig.maxEmptyLines,
                maxEmptyLinesIsEnabled = oldConfig.maxEmptyLinesIsEnabled,
                removeTrailingCommas = oldConfig.removeTrailingCommas,
                majorVersion = DartFormatConfig.CURRENT_MAJOR_VERSION,
                minorVersion = DartFormatConfig.CURRENT_MINOR_VERSION
            )
        }
    }
}
