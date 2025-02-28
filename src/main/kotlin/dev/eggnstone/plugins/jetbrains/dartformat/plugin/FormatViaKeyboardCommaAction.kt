package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.*
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class FormatViaKeyboardCommaAction : AnAction()
{
    companion object
    {
        const val CLASS_NAME = "FormatViaKeyboardCommaAction"
    }

    init
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("$CLASS_NAME.init()")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        if (Constants.LOG_VERBOSE) Logger.logVerbose("${CLASS_NAME}.actionPerformed()")
        FormatAction().actionPerformed(e, useBuiltInFormatter = false)
    }
}
