package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class DartFormatConfigUpdater
{
    companion object
    {
        fun updateV1toV2(oldConfig: DartFormatConfigV1): DartFormatConfigV2
        {
            Logger.logDebug("DartFormatConfigUpdater.updateV1toV2: updating V1 to V2")
            return DartFormatConfigV2(
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
                removeTrailingCommas = oldConfig.removeTrailingCommas
            )
        }
    }
}
