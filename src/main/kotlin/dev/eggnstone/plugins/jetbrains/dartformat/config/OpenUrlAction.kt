package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.awt.Desktop
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.net.URI

class OpenUrlAction(private val uri: URI) : MouseAdapter()
{
    override fun mouseClicked(e: MouseEvent?)
    {
        Logger.logDebug("OpenUrlAction.mouseClicked: $e")
        Desktop.getDesktop().browse(uri)
    }
}
