package com.eggnstone.jetbrainsplugins.dartformat.config

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "DartFormatPersistentStateComponent",
    storages = [Storage("DartFormatPlugin.xml")]
)
class DartFormatPersistentStateComponent : PersistentStateComponent<DartFormatState>
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

    private var dartFormatState = DartFormatState()

    override fun getState(): DartFormatState
    {
        return dartFormatState
    }

    override fun loadState(state: DartFormatState)
    {
        dartFormatState = state
    }
}
