package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "DartFormat",
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponentV2 : PersistentStateComponent<DartFormatConfigV2>
{
    companion object
    {
        val instance: DartFormatPersistentStateComponentV2?
            get()
            {
                return ApplicationManager.getApplication().getService(DartFormatPersistentStateComponentV2::class.java)
            }
    }

    private var dartFormatConfig = DartFormatConfigV2()

    override fun getState(): DartFormatConfigV2 = dartFormatConfig

    override fun noStateLoaded()
    {
        super.noStateLoaded()
        dartFormatConfig = DartFormatConfigV2()
        dartFormatConfig.setLoaded(false)
    }

    override fun loadState(state: DartFormatConfigV2)
    {
        dartFormatConfig = state
        dartFormatConfig.setLoaded(true)
    }

    fun clearState()
    {
        dartFormatConfig = DartFormatConfigV2()
        dartFormatConfig.setLoaded(null)
    }
}
