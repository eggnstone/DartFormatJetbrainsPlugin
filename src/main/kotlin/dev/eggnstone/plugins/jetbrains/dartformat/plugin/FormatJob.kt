package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.Job

class FormatJob(val project: Project, val command: String, val config: String?, val inputText: String?) : CompletableJob by Job()
{
    var formatResult: FormatResult? = null
}
