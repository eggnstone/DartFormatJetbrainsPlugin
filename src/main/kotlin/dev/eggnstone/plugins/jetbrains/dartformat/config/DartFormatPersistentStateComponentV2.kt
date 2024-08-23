package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

@State(
    name = "DartFormat",
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponentV2 : PersistentStateComponent<DartFormatConfig>
{
    companion object
    {
        val instance: DartFormatPersistentStateComponentV2?
            get()
            {
                return try
                {
                    if (Constants.DEBUG_CONFIG) Logger.logDebug("DartFormatPersistentStateComponentV2.instance")
                    ApplicationManager.getApplication().getService(DartFormatPersistentStateComponentV2::class.java)
                }
                catch (e: Exception)
                {
                    Logger.logError("DartFormatPersistentStateComponentV2.instance: $e")
                    null
                }
            }
    }

    private var dartFormatConfig = DartFormatConfig.default(version = 2)

    override fun getState(): DartFormatConfig
    {
        return dartFormatConfig
    }

    override fun loadState(state: DartFormatConfig)
    {
        try
        {
            if (Constants.DEBUG_CONFIG) Logger.logDebug("DartFormatPersistentStateComponentV2.loadState")
            dartFormatConfig = state
        }
        catch (e: Exception)
        {
            Logger.logError("DartFormatPersistentStateComponentV2.loadState: $e")
            dartFormatConfig = DartFormatConfig.default(version = 2)
        }
    }
}
