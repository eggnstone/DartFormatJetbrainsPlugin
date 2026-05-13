package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class ProjectActivity : com.intellij.openapi.startup.ProjectActivity
{
    companion object
    {
        // Companion init runs once at class-load time. Removing "ReformatCode" from the
        // CodeFormatGroup is a global mutation that only needs to happen once per IDE session;
        // putting it here avoids repeating it on every project open (the instance init { } block
        // does run every time IntelliJ constructs a new ProjectActivity).
        init
        {
            val actionManager = ActionManager.getInstance()
            val actionGroup: DefaultActionGroup = actionManager.getAction("CodeFormatGroup") as DefaultActionGroup
            actionGroup.remove(actionManager.getAction("ReformatCode"))
        }
    }

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("ProjectActivity.init()")
    }

    override suspend fun execute(project: Project)
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("ProjectActivity.execute()")
        // Touch the service so its constructor runs and launches run() on the injected scope.
        // The cast-to-Unit silences the unused-return warning for the side-effecting call.
        ExternalDartFormat.getInstance()
    }
}
