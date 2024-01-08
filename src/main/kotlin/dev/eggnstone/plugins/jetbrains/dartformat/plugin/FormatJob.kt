package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job

class FormatJob(val command: String, val config: String?, val inputText: String?) : CompletableJob by Job()
{
    var formatResult: FormatResult? = null
}
