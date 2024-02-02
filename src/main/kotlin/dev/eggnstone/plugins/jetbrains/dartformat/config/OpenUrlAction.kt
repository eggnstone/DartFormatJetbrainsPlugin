package dev.eggnstone.plugins.jetbrains.dartformat.config

import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import java.awt.Desktop
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.net.URI

class OpenUrlAction(private val uri: URI) : MouseListener
{
    override fun mouseClicked(e: MouseEvent?)
    {
        Logger.log("OpenUrlAction.mouseClicked: $e")
        val desktop: Desktop = Desktop.getDesktop()
        desktop.browse(uri)
    }

    override fun mousePressed(e: MouseEvent?)
    {
    }

    override fun mouseReleased(e: MouseEvent?)
    {
    }

    override fun mouseEntered(e: MouseEvent?)
    {
    }

    override fun mouseExited(e: MouseEvent?)
    {
    }
}
