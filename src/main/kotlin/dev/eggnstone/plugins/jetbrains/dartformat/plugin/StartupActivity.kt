package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import kotlinx.coroutines.runBlocking

class StartupActivity : com.intellij.openapi.startup.StartupActivity
{
    init
    {
        Logger.logDebug("StartupActivity.init()")

        runBlocking {
            T().f()
        }
    }

    override fun runActivity(project: Project)
    {
        Logger.logDebug("StartupActivity.runActivity()")
        ExternalDartFormat.instance.init()
    }
}
