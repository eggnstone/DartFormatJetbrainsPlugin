package com.eggnstone.jetbrainsplugins.dartformat

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class PluginFormatAllFilesInCurrentFolder : AnAction()
{
    init
    {
        //println("PluginFormatAllFilesInCurrentFolder.CTOR")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        println("PluginFormatAllFilesInCurrentFolder.actionPerformed: TODO")
    }

    override fun update(e: AnActionEvent)
    {
        println("PluginFormatAllFilesInCurrentFolder.update: TODO")
    }
}
