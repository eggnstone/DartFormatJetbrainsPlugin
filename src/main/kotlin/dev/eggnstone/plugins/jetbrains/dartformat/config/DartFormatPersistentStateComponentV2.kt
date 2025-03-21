package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

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
                return ApplicationManager.getApplication().getService(DartFormatPersistentStateComponentV2::class.java)
            }
    }

    private var dartFormatConfig: DartFormatConfig? = null

    override fun getState(): DartFormatConfig = dartFormatConfig!!

    override fun noStateLoaded()
    {
        super.noStateLoaded()
        dartFormatConfig = DartFormatConfig.current()
        dartFormatConfig!!.setLoaded(false)
    }

    override fun loadState(state: DartFormatConfig)
    {
        dartFormatConfig = state
        dartFormatConfig!!.setLoaded(true)
    }

    /*fun clearState()
    {
        dartFormatConfig = DartFormatConfig()
        dartFormatConfig!!.setLoaded(null)
    }*/
}
