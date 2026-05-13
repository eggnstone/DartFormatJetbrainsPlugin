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

    // lateinit so a stray getState() before loadState()/noStateLoaded() fails with a clear
    // UninitializedPropertyAccessException instead of an opaque NPE. In practice IntelliJ always
    // calls one of those first, but the previous nullable + !! made the contract fragile.
    private lateinit var dartFormatConfig: DartFormatConfig

    override fun getState(): DartFormatConfig = dartFormatConfig

    override fun noStateLoaded()
    {
        super.noStateLoaded()
        dartFormatConfig = DartFormatConfig.current().apply { setLoaded(false) }
    }

    override fun loadState(state: DartFormatConfig)
    {
        dartFormatConfig = state.apply { setLoaded(true) }
    }
}
