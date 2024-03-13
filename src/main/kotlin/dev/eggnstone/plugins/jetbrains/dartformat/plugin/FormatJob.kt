package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.vfs.VirtualFile
import kotlinx.coroutines.Job

// : CompletableJob by Job() => plugin verification error: Abstract method Job.getParent() is not implemented
class FormatJob(val command: String, val config: String?, val inputText: String?, val virtualFile: VirtualFile?)
{
    private val job = Job()

    var formatResult: FormatResult? = null

    fun cancel() = job.cancel()

    fun complete() = job.complete()

    suspend fun join() = job.join()
}
