package com.eggnstone.jetbrainsplugins.dartformat.config

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel

class DartFormatPersistentStateConfigurable : Configurable, Disposable
{
    private var removeUnnecessaryCommasCheckbox: JCheckBox? = JCheckBox("Remove unnecessary commas")

    private val configState: DartFormatConfig? get() = DartFormatPersistentStateComponent.instance?.state

    override fun apply()
    {
        if (configState == null)
        {
            println("Error in apply: configState == null")
            return
        }

        configState!!.removeUnnecessaryCommas = removeUnnecessaryCommasCheckbox!!.isSelected
    }

    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(removeUnnecessaryCommasCheckbox) })

        return JPanel(BorderLayout()).also { it.add(formBuilder.panel, BorderLayout.NORTH) }
    }

    override fun dispose()
    {
        removeUnnecessaryCommasCheckbox = null
    }

    override fun getDisplayName(): String = "DartFormat"

    override fun isModified(): Boolean
    {
        if (configState == null)
        {
            println("Error in isModified: configState == null")
            return false
        }

        return configState!!.removeUnnecessaryCommas != removeUnnecessaryCommasCheckbox!!.isSelected
    }

    override fun reset()
    {
        if (configState == null)
        {
            println("Error in reset: configState == null")
            return
        }

        removeUnnecessaryCommasCheckbox!!.isSelected = configState!!.removeUnnecessaryCommas
    }
}
