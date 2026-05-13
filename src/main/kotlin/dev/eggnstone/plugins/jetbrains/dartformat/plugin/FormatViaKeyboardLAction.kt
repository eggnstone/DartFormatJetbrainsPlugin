package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.ActionPromoter
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataContext
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class FormatViaKeyboardLAction : AnAction(), ActionPromoter
{
    companion object
    {
        const val CLASS_NAME = "FormatViaKeyboardLAction"
    }

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.init()")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.actionPerformed()")

        FormatAction().actionPerformed(e, useBuiltInFormatter = true)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun promote(actions: MutableList<out AnAction>, context: DataContext): MutableList<AnAction>
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.promote()")
        return actions.filterIsInstance<FormatViaKeyboardLAction>().toMutableList()
    }

    override fun suppress(actions: MutableList<out AnAction>, context: DataContext): MutableList<AnAction>
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.suppress()")
        return actions.filter { it !is FormatViaKeyboardLAction }.toMutableList()
    }
}
