package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.data.NotificationInfo
import dev.eggnstone.plugins.jetbrains.dartformat.tools.NotificationTools

class FormatAction : AnAction()
{
    override fun actionPerformed(e: AnActionEvent)
    {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val config = getConfig()
        if (!checkConfig(project, config))
            return

        val formatter = Formatter(project, config)
        val selectedVirtualFiles = e.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        formatter.format(selectedVirtualFiles)
    }

    private fun checkConfig(project: Project, config: DartFormatConfig): Boolean
    {
        if (config.hasNothingEnabled())
        {
            val title = "No formatting option enabled"
            val content = "Please enable your desired formatting options:" +
                "<pre>File -&gt; Settings -&gt; Other Settings -&gt; DartFormat</pre>"
            NotificationTools.notifyWarning(NotificationInfo(
                content = content,
                fileName = null,
                links = null,
                origin = null,
                project = project,
                title = title
            ))

            return false
        }

        if (!config.acceptBeta)
        {
            val title = "Beta version not accepted"
            val content = "<html><body>" +
                "Please accept that this is a beta version and not everything works as it should:" +
                "<pre>File -&gt; Settings -&gt; Other Settings -&gt; DartFormat</pre>" +
                "</body></html>"
            NotificationTools.notifyWarning(NotificationInfo(
                content = content,
                fileName = null,
                links = null,
                origin = null,
                project = project,
                title = title
            ))

            return false
        }

        return true
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
