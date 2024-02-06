package dev.eggnstone.plugins.jetbrains.dartformat.tools

class StringTools
{
    companion object
    {
        fun toDisplayString(s: String, maxLength: Int = -1): String = "\"${toSafeString(s, maxLength)}\""

        // TODO: use toPipedText for all logging?
        fun toPipedText(s: String): String
        {
            val r = s
                .replace("\n", "|")
                .replace("\r", "|")
                .replace("||", "|")

            return if (r.endsWith("|")) r.substring(0, r.length - 1) else r
        }

        @Suppress("MemberVisibilityCanBePrivate")
        fun toSafeString(o: Any?, maxLength: Int = -1): String
        {
            if (o == null)
                return "<null>"

            val s = o.toString()

            var r = s.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")
            if (maxLength >= 0 && r.length > maxLength)
                r = "${r.substring(0, maxLength)}..."

            return r
        }
    }
}
