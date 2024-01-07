package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job

class FormatJob(val command: String, val inputText: String) : CompletableJob by Job()
{
    var outputText: String? = null
    var errorText: String? = null
}