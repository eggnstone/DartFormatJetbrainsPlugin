package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

class VersionTools
{
    companion object
    {
        fun getVersion(): String
        {
            val id = PluginId.getId("com.eggnstone.jetbrainsplugins.DartFormat")
            val pluginDescriptor = PluginManagerCore.getPlugin(id)

            return pluginDescriptor?.version ?: "<unknown version>"
        }
    }
}
