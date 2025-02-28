package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.*
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
        //val reformatAction = ActionManager.getInstance().getAction(IdeActions.ACTION_EDITOR_REFORMAT)
        //reformatAction.actionPerformed(e)
    }

    override fun promote(actions: MutableList<out AnAction>, context: DataContext): MutableList<AnAction>?
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.promote()")

        val fixedActions: MutableList<AnAction> = mutableListOf()
        for (action in actions)
            if (action.javaClass.simpleName == "FormatViaKeyboardLAction")
                fixedActions.add(action)

        //return super.promote(actions, context)
        return fixedActions
    }

    override fun suppress(actions: MutableList<out AnAction>, context: DataContext): MutableList<AnAction>?
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.suppress()")

        val fixedActions: MutableList<AnAction> = mutableListOf()
        for (action in actions)
            if (action.javaClass.simpleName != "FormatViaKeyboardLAction")
                fixedActions.add(action)

        //return super.suppress(actions, context)
        return fixedActions
    }
}
