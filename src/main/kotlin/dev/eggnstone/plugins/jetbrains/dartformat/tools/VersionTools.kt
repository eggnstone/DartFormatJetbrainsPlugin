package dev.eggnstone.plugins.jetbrains.dartformat.tools

import java.io.InputStream
import java.util.*

class VersionTools
{
    companion object
    {
        fun getVersion(): String
        {
            val inputStream: InputStream? = this::class.java.getResourceAsStream("/version.properties")
            @Suppress("FoldInitializerAndIfToElvis", "RedundantSuppression")
            if (inputStream == null)
                return "<unknown version>"

            val properties = Properties()
            properties.load(inputStream)
            return properties.getProperty("version", "<unknown version>")
        }
    }
}
