package dev.eggnstone.plugins.jetbrains.dartformat.data

data class Version(val major: Int, val minor: Int, val patch: Int)
{
    override fun toString(): String = "$major.$minor.$patch"

    companion object
    {
        @Suppress("MemberVisibilityCanBePrivate")
        fun parse(s: String): Version
        {
            val parts = s.split(".")
            val major = parts[0].toInt()
            val minor = parts[1].toInt()
            val patch = parts[2].toInt()

            return Version(major, minor, patch)
        }

        fun parseOrNull(s: String?): Version?
        {
            if (s == null)
                return null

            return try
            {
                parse(s)
            }
            catch (e: Exception)
            {
                null
            }
        }
    }

    fun isOlderThan(otherVersion: Version?): Boolean
    {
        if (otherVersion == null)
            return false

        if (major < otherVersion.major)
            return true

        if (major > otherVersion.major)
            return false

        if (minor < otherVersion.minor)
            return true

        if (minor > otherVersion.minor)
            return false

        return patch < otherVersion.patch
    }
}
