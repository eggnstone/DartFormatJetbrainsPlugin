package dev.eggnstone.plugins.jetbrains.dartformat.tools

import java.io.InputStream

class VersionTools
{
    companion object
    {
        private const val UNKNOWN_VERSION: String = "(unknown version)"
        private val VERSION_REGEX: Regex = Regex("<version>([^<]+)</version>")

        fun getVersion(): String
        {
            val stream: InputStream = VersionTools::class.java.getResourceAsStream("/META-INF/plugin.xml")
                ?: return UNKNOWN_VERSION
            val content: String = stream.use { it.reader(Charsets.UTF_8).readText() }
            val match: MatchResult = VERSION_REGEX.find(content) ?: return UNKNOWN_VERSION
            return match.groupValues[1]
        }
    }
}
