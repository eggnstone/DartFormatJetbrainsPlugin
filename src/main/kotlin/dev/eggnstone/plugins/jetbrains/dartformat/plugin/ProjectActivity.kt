package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfigGetter
import dev.eggnstone.plugins.jetbrains.dartformat.data.LinkInfo
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools

class ProjectActivity : com.intellij.openapi.startup.ProjectActivity
{
    companion object
    {
        private const val ACTION_ID_FORMAT_DART = "dev.eggnstone.plugins.jetbrains.dartformat.FormatViaKeyboardComma"
        private const val ACTION_ID_FORMAT_ALL = "dev.eggnstone.plugins.jetbrains.dartformat.FormatViaKeyboardL"

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

        showWelcomeIfFirstTime(project)
    }

    private fun showWelcomeIfFirstTime(project: Project)
    {
        val config = DartFormatConfigGetter.get()
        if (config.welcomeShown && !Constants.DEBUG_FAKE_SHOW_WELCOME)
            return

        // Resolve shortcuts from the live keymap rather than hardcoding plugin.xml's defaults, so
        // users who rebind in Settings → Keymap see the binding they actually have. Empty when an
        // action is unbound; we just omit that line in that case.
        val dartShortcut = KeymapUtil.getFirstKeyboardShortcutText(ACTION_ID_FORMAT_DART)
        val allShortcut = KeymapUtil.getFirstKeyboardShortcutText(ACTION_ID_FORMAT_ALL)

        val lines = mutableListOf<String>()
        if (dartShortcut.isNotEmpty())
            lines += "Press $dartShortcut to format Dart files."

        if (allShortcut.isNotEmpty())
            lines += "Press $allShortcut to format all file types (Dart via dart_format, others via the built-in formatter)."

        lines += "Configure under Settings → DartFormat."

        NotificationTools.notifyInfo(
            NotificationInfo(
                content = lines.joinToString("\n"),
                links = listOf(LinkInfo("Open Settings", "action://openSettings")),
                origin = null,
                project = project,
                title = "Welcome to DartFormat!",
                virtualFile = null
            )
        )

        if (!Constants.DEBUG_FAKE_SHOW_WELCOME)
            config.welcomeShown = true
    }
}
