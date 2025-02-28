package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class ProjectActivity : com.intellij.openapi.startup.ProjectActivity
{
    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("ProjectActivity.init()")

        val actionManager = ActionManager.getInstance()
        val actionGroup: DefaultActionGroup = actionManager.getAction("CodeFormatGroup") as DefaultActionGroup
        actionGroup.remove(actionManager.getAction("ReformatCode"))
    }

    override suspend fun execute(project: Project)
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("ProjectActivity.execute()")
        ExternalDartFormat.instance.init()
    }
}
