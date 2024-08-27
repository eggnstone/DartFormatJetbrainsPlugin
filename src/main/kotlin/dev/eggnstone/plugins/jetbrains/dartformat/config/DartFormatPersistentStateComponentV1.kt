package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "DartFormatPersistentStateComponent",
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponentV1 : PersistentStateComponent<DartFormatConfigV1>
{
    companion object
    {
        val instance: DartFormatPersistentStateComponentV1?
            get()
            {
                return ApplicationManager.getApplication().getService(DartFormatPersistentStateComponentV1::class.java)
            }
    }

    private var dartFormatConfig = DartFormatConfigV1()

    override fun getState(): DartFormatConfigV1 = dartFormatConfig

    override fun noStateLoaded()
    {
        super.noStateLoaded()
        dartFormatConfig = DartFormatConfigV1()
        dartFormatConfig.setLoaded(false)
    }

    override fun loadState(state: DartFormatConfigV1)
    {
        dartFormatConfig = state
        dartFormatConfig.setLoaded(true)
    }

    fun clearState()
    {
        dartFormatConfig = DartFormatConfigV1()
        dartFormatConfig.setLoaded(null)
    }
}
