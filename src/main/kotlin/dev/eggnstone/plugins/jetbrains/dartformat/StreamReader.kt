package dev.eggnstone.plugins.jetbrains.dartformat

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class StreamReader(inputStream: InputStream)
{
    // dart_format speaks ASCII over the pipe, but `$SHELL -ilc` (added so .zshrc/.bashrc sets dart
    // on PATH) routes the user's rc-file output through here — locale banners / starship prompts /
    // emoji are routinely non-ASCII. Reading byte by byte and casting Int → Char would interpret
    // each byte as latin-1, mangling any UTF-8 multibyte sequence (e.g. `C3 84` for `Ä` → `Ã` +
    // invisible control char). Wrap once in a UTF-8 reader and operate on chars throughout.
    private val reader = BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8))

    fun readLine(): String = reader.readLine() ?: ""

    // BufferedReader.ready() is true if its internal char buffer has data OR the underlying stream
    // has bytes ready. The only caller (TimedReader.receiveLine) just branches on > 0 vs not, so
    // we don't need an actual byte count.
    fun available(): Int = if (reader.ready()) 1 else 0

    // Blocking drain until EOF. Use only after the process has exited so the pipe is closed;
    // otherwise this will block indefinitely. Note `available()` can return 0 right after exit
    // even when bytes remain (some in the BufferedReader's char buffer, some in the kernel pipe
    // not yet pulled), so we must read past it to surface stderr from crashed shim scripts.
    fun drainToEof(): String
    {
        val sb = StringBuilder()
        val buf = CharArray(1024)
        while (true)
        {
            val n = reader.read(buf)
            if (n == -1)
                break
            sb.append(buf, 0, n)
        }
        return sb.toString()
    }
}
