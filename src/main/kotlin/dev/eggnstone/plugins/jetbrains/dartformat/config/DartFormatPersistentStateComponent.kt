package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "DartFormatPersistentStateComponent",
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
                catch (e: NullPointerException)
                {
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
        dartFormatConfig = state
    }
}
