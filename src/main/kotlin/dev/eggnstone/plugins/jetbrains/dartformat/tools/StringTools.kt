package dev.eggnstone.plugins.jetbrains.dartformat.tools

class StringTools
{
    companion object
    {
        fun toDisplayString(s: String, maxLength: Int = -1): String = "\"${toSafeString(s, maxLength)}\""

        // TODO: use toTextWithPipes for all logging?
        fun toTextWithPipes(s: String): String
        {
            var r = s.replace("\n", "|").replace("\r", "|")

            while (r.contains("||"))
                r = r.replace("||", "|")

            while (r.isNotEmpty() && r.startsWith("|"))
                r = r.substring(1)

            if (r.isNotEmpty() && r.endsWith("|"))
                r = r.substring(0, r.length - 1)

            return r
        }

        fun toTextWithHtmlBreaks(s: String): String = toTextWithPipes(s).replace("|", "<br/>")

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
