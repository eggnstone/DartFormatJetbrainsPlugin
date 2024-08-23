package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DartFormatConfigGetter
{
    companion object
    {
        fun get(): DartFormatConfig
        {
            if (DartFormatPersistentStateComponentV2.instance != null)
            {
                if (Constants.DEBUG_CONFIG) Logger.logDebug("FormatAction.getConfig: using existing V2")

                try
                {
                    val x: DartFormatConfig = DartFormatPersistentStateComponentV2.instance!!.state
                    if (Constants.DEBUG_CONFIG) Logger.logDebug(x.toJson())
                    val json = Json.encodeToString(x)
                    if (Constants.DEBUG_CONFIG) Logger.logDebug(json)
                }
                catch (e: Exception)
                {
                    Logger.logError("DartFormatPersistentStateConfigurable.getConfig: $e")
                }

                return DartFormatPersistentStateComponentV2.instance!!.state
            }

            if (DartFormatPersistentStateComponentV1.instance != null)
            {
                if (Constants.DEBUG_CONFIG) Logger.logDebug("FormatAction.getConfig: using existing V1")
                return DartFormatPersistentStateComponentV1.instance!!.state
            }

            if (Constants.DEBUG_CONFIG) Logger.logDebug("FormatAction.getConfig: using new V2")
            return DartFormatConfig.default(version = 2)
        }
    }
}
