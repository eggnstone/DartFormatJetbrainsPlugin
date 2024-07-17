package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class ProjectActivity : com.intellij.openapi.startup.ProjectActivity
{
    init
    {
        Logger.logDebug("ProjectActivity.init()")
    }

    override suspend fun execute(project: Project)
    {
        Logger.logDebug("ProjectActivity.execute()")
        ExternalDartFormat.instance.init()
    }
}
