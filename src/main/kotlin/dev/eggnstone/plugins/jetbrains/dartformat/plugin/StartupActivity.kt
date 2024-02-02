package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class StartupActivity : com.intellij.openapi.startup.StartupActivity
{
    init
    {
        Logger.log("StartupActivity.init()")
    }

    override fun runActivity(project: Project)
    {
        Logger.log("StartupActivity.runActivity()")
        ExternalDartFormat.instance.init(project)
    }
}
