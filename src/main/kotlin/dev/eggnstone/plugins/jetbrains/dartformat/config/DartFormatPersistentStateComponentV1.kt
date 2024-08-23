package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

@State(
    name = "DartFormatPersistentStateComponent",
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponentV1 : PersistentStateComponent<DartFormatConfig>
{
    companion object
    {
        val instance: DartFormatPersistentStateComponentV1?
            get()
            {
                return try
                {
                    if (Constants.DEBUG_CONFIG) Logger.logDebug("DartFormatPersistentStateComponentV1.instance")
                    ApplicationManager.getApplication().getService(DartFormatPersistentStateComponentV1::class.java)
                }
                catch (e: Exception)
                {
                    Logger.logError("DartFormatPersistentStateComponentV1.instance: $e")
                    null
                }
            }
    }

    private var dartFormatConfig = DartFormatConfig.none(version = 1)

    override fun getState(): DartFormatConfig
    {
        return dartFormatConfig
    }

    override fun loadState(state: DartFormatConfig)
    {
        try
        {
            if (Constants.DEBUG_CONFIG) Logger.logDebug("DartFormatPersistentStateComponentV1.loadState")
            dartFormatConfig = state
        }
        catch (e: Exception)
        {
            Logger.logError("DartFormatPersistentStateComponentV1.loadState: $e")
            dartFormatConfig = DartFormatConfig.none(version = 1)
        }
    }
}
