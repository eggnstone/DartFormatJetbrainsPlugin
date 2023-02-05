package com.eggnstone.jetbrainsplugins.dartformat.plugin

import com.eggnstone.jetbrainsplugins.dartformat.formatter.Formatter
import com.eggnstone.jetbrainsplugins.dartformat.tokenizer.Tokenizer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager

class PluginFormatCurrentFile : AnAction()
{
    private var enabled: Boolean = false

    init
    {
        //println("DartFormat.CTOR")
    }

    override fun actionPerformed(e: AnActionEvent)
    {
        if (!enabled)
            return

        ApplicationManager.getApplication().runWriteAction {
            val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
            val tokens = Tokenizer().tokenize(editor.document.text)
            val formattedText = Formatter().format(tokens)
            editor.document.setText(formattedText)
        }
    }

    override fun update(e: AnActionEvent)
    {
        val editor: Editor = e.getRequiredData(CommonDataKeys.EDITOR)
        val document = editor.document

        if (!document.isWritable)
        {
            enabled = false
            return
        }

        val file = FileDocumentManager.getInstance().getFile(document)
        enabled = file?.extension == "dart"
    }
}
