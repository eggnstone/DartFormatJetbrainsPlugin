package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

@State(
    name = "DartFormatPersistentStateComponent", // TODO: rename because it appears in "export settings" dialog
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponent : PersistentStateComponent<DartFormatConfig>
{
    companion object
    {
        val instance: DartFormatPersistentStateComponent?
            get()
            {
                return try
                {
                    ApplicationManager.getApplication().getService(DartFormatPersistentStateComponent::class.java)
                }
                catch (e: Exception)
                {
                    Logger.logError("DartFormatPersistentStateComponent.instance: $e")
                    null
                }
            }
    }

    private var dartFormatConfig = DartFormatConfig()

    override fun getState(): DartFormatConfig
    {
        return dartFormatConfig
    }

    override fun loadState(state: DartFormatConfig)
    {
        try
        {
            dartFormatConfig = state
        }
        catch (e: Exception)
        {
            Logger.logError("DartFormatPersistentStateComponent.loadState: $e")
            dartFormatConfig = DartFormatConfig()
        }
    }
}
