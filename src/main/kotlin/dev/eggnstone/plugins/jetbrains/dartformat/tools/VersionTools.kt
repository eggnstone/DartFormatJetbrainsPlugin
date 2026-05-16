package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.ide.plugins.PluginManager

class VersionTools
{
    companion object
    {
        fun getVersion(): String
        {
            val pluginDescriptor = PluginManager.getPluginByClass(VersionTools::class.java)
            return pluginDescriptor?.version ?: "(unknown version)"
        }
    }
}
