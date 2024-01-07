package dev.eggnstone.plugins.jetbrains.dartformat.pseudo_http

class PseudoHttpResult(
    val statusCode: Int,
    val status: String,
    @Suppress("MemberVisibilityCanBePrivate")
    val headers: List<String>,
    val body: ByteArray? = null
)
{
    fun getHeaderInt(key: String, default: Int): Int
    {
        val searchKey = "$key: "
        val header = headers.find { it.startsWith(searchKey) }
        if (header != null)
        {
            val value = header.substring(searchKey.length).trim()
            return value.toIntOrNull() ?: default
        }

        return default
    }
}
