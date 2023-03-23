package dev.eggnstone.plugins.jetbrains.dartformat.plugin

import com.intellij.notification.*
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ContentIterator
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.BalloonImpl
import com.intellij.util.ui.JBUI
import dev.eggnstone.plugins.jetbrains.dartformat.DartFormatException
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatConfig
import dev.eggnstone.plugins.jetbrains.dartformat.config.DartFormatPersistentStateComponent
import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger
import dev.eggnstone.plugins.jetbrains.dartformat.indenters.iIndenters.MasterIndenter
import dev.eggnstone.plugins.jetbrains.dartformat.splitters.iSplitters.MasterSplitter
import java.awt.Font
import java.util.*
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel

typealias FormatHandler = (virtualFile: VirtualFile, project: Project, lines: MutableList<String>) -> Boolean

class PluginFormat : AnAction()
{
    companion object
    {
        private val masterSplitter = MasterSplitter()
    }

    private class FormatIterator(private val format: FormatHandler, private val project: Project, private val lines: MutableList<String>) : ContentIterator
    {
        override fun processFile(virtualFile: VirtualFile): Boolean
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("FormatIterator.processFile: $virtualFile")
            return format(virtualFile, project, lines)
        }
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)

        val config = getConfig()
        if (!config.isEnabled)
        {
            val title = "DartFormat"
            val subtitle = "No formatting option enabled"
            val messages = listOf("Please check File -&gt; Settings -&gt; Other Settings -&gt; DartFormat")
            notifyWarning(title, subtitle, messages, project, null)
            return
        }

        val startTime = Date()

        val editor = e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE)

        try
        {
            val lines = mutableListOf<String>()
            val formatIterator = FormatIterator(this::formatDartFile, project, lines)
            val virtualFiles = e.getRequiredData(CommonDataKeys.VIRTUAL_FILE_ARRAY)

            CommandProcessor.getInstance().runUndoTransparentAction {
                for (virtualFile in virtualFiles)
                {
                    // TODO: filter out already visited files!
                    //if (DotlinLogger.isEnabled) DotlinLogger.log("\n  $virtualFile")
                    VfsUtilCore.iterateChildrenRecursively(virtualFile, this::filterDartFiles, formatIterator)
                }
            }

            val endTime = Date()
            val diffTime = endTime.time - startTime.time
            var filesText = "file"
            if (virtualFiles.size != 1)
                filesText += "s"

            lines.add(0, "Formatting ${virtualFiles.size} $filesText took $diffTime ms.")
            notify("DartFormat", null, project, editor, lines)
        }
        catch (err: DartFormatException)
        {
            val errMessage = err.message ?: "Unknown error."
            notifyError("DartFormat", null, project, editor, listOf("Error while formatting:", errMessage))
        }
        catch (err: AssertionError)
        {
            notifyError("DartFormat", null, project, editor, listOf("Error while formatting:", err.toString()))
        }
    }

    private fun notify(title: String, subtitle: String?, project: Project, editor: Editor?, lines: List<String>)
    {
        notifyByToolWindowBalloon(title, subtitle, lines, NotificationType.INFORMATION, project)
        //notifyByEditorPopup(editor, lines)
    }

    private fun notifyWarning(title: String, subtitle: String?, lines: List<String>, project: Project, editor: Editor?)
    {
        notifyByToolWindowBalloon(title, subtitle, lines, NotificationType.WARNING, project)
        //notifyByEditorPopup(editor, lines)
    }

    private fun notifyError(title: String, subtitle: String?, project: Project, editor: Editor?, lines: List<String>)
    {
        notifyByToolWindowBalloon(title, subtitle, lines, NotificationType.ERROR, project)
        //notifyByEditorPopup(editor, lines)
    }

    private fun notifyByToolWindowBalloon(title: String, subtitle: String?, lines: List<String>, type: NotificationType, project: Project)
    {
        val combinedLines = lines.joinToString("<br/>")

        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("DartFormat Balloon Notifications")
        val notification = notificationGroup.createNotification(title, combinedLines, type)
        notification.subtitle = subtitle
        notification.notify(project)
    }

    private fun notifyByEditorPopup(editor: Editor?, lines: List<String>)
    {
        if (editor == null)
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("editor == null")
            //val combinedLines = lines.joinToString("\n")
            //if (DotlinLogger.isEnabled) DotlinLogger.log(combinedLines)
            return
        }

        val factory = JBPopupFactory.getInstance()

        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        panel.border = JBUI.Borders.empty(4)

        val title = JLabel("DartFormat")
        title.font = Font(title.font.name, Font.BOLD, 14)
        title.border = JBUI.Borders.emptyBottom(4)
        panel.add(title)

        for (line in lines)
            panel.add(JLabel(line))

        val balloonBuilder = factory.createBalloonBuilder(panel)
        val balloon = balloonBuilder.createBalloon()
        val position = factory.guessBestPopupLocation(editor)

        if (balloon is BalloonImpl)
            balloon.setFillColor(panel.background)

        balloon.show(position, Balloon.Position.below)
    }

    private fun filterDartFiles(virtualFile: VirtualFile): Boolean = virtualFile.isDirectory || isDartFile(virtualFile)

    private fun isDartFile(virtualFile: VirtualFile): Boolean
    {
        if (virtualFile.extension != "dart")
            return false

        if (virtualFile.name.endsWith(".freezed.dart"))
            return false

        if (virtualFile.name.endsWith(".g.dart"))
            return false

        if (virtualFile.name.endsWith(".gr.dart"))
            return false

        return true
    }

    private fun formatDartFile(virtualFile: VirtualFile, project: Project, lines: MutableList<String>): Boolean
    {
        if (!isDartFile(virtualFile))
        {
            //if (DotlinLogger.isEnabled) DotlinLogger.log("formatDartFile: $virtualFile")
            //if (DotlinLogger.isEnabled) DotlinLogger.log("  Not a dart file!")
            return true
        }

        try
        {
            val fileEditor = FileEditorManager.getInstance(project).getSelectedEditor(virtualFile)
            @Suppress("FoldInitializerAndIfToElvis")
            if (fileEditor == null)
                return formatDartFileByBinaryContent(virtualFile)

            return formatDartFileByFileEditor(fileEditor, lines)
        }
        catch (err: DartFormatException)
        {
            throw DartFormatException("1 $virtualFile: ${err.message}")
            /*
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $virtualFile:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
            */
        }
        catch (err: AssertionError)
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $virtualFile:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
        }
    }

    private fun formatDartFileByBinaryContent(virtualFile: VirtualFile): Boolean
    {
        try
        {
            if (!virtualFile.isWritable)
            {
                if (DotlinLogger.isEnabled) DotlinLogger.log("formatDartFileByBinaryContent: $virtualFile")
                if (DotlinLogger.isEnabled) DotlinLogger.log("  !virtualFile.isWritable")
                return false
            }

            val inputBytes = virtualFile.inputStream.readAllBytes()
            val inputText = String(inputBytes)
            val outputText = format(inputText)
            if (outputText == inputText)
                return true

            val outputBytes = outputText.toByteArray()
            ApplicationManager.getApplication().runWriteAction {
                virtualFile.setBinaryContent(outputBytes)
            }

            return true
        }
        catch (err: DartFormatException)
        {
            throw DartFormatException("2 $virtualFile: ${err.message}")
            /*
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $virtualFile:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
            */
        }
        catch (err: AssertionError)
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $virtualFile:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
        }
    }

    private fun formatDartFileByFileEditor(fileEditor: FileEditor, lines: MutableList<String>): Boolean
    {
        if (fileEditor !is TextEditor)
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("formatDartFileByFileEditor: $fileEditor")
            if (DotlinLogger.isEnabled) DotlinLogger.log("  fileEditor !is TextEditor")
            return false
        }

        val editor = fileEditor.editor

        try
        {
            val document = editor.document
            val inputText = document.text
            val outputText = format(inputText)
            if (outputText == inputText)
            {
                lines += "Nothing changed."
                return true
            }

            ApplicationManager.getApplication().runWriteAction {
                document.setText(outputText)
            }

            lines += "Something changed."
            return true
        }
        catch (err: DartFormatException)
        {
            throw DartFormatException("$fileEditor: ${err.message}")
            /*
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $virtualFile:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
            */
        }
        catch (err: AssertionError)
        {
            if (DotlinLogger.isEnabled) DotlinLogger.log("While formatting: $fileEditor:")
            if (DotlinLogger.isEnabled) DotlinLogger.log("$err")
            return false
        }
    }

    private fun format(inputText: String): String
    {
        DotlinLogger.isEnabled = false

        try
        {
            val config = getConfig()

            val splitResult = masterSplitter.split(inputText)
            //PartTools.printParts(splitResult.parts)

            val masterIndenter = MasterIndenter(config.indentationSpacesPerLevel)

            @Suppress("UnnecessaryVariable")
            val indentResult = masterIndenter.indentParts(splitResult.parts)

            return indentResult
        }
        finally
        {
            DotlinLogger.isEnabled = true
        }
    }

    private fun getConfig(): DartFormatConfig
    {
        if (DartFormatPersistentStateComponent.instance == null)
            return DartFormatConfig()

        return DartFormatPersistentStateComponent.instance!!.state
    }
}
