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
    private var removeUnnecessaryLineBreaksAfterArrowsCheckbox: JCheckBox? = JCheckBox("Remove unnecessary line breaks after arrows")

    private var indentationIsEnabledCheckbox: JCheckBox? = JCheckBox("Indent (currently fixed to 4 spaces)")

    private val config: DartFormatConfig? get() = DartFormatPersistentStateComponent.instance?.state

    override fun apply()
    {
        if (config == null)
        {
            println("Error in apply: configState == null")
            return
        }

        config!!.removeUnnecessaryCommas = removeUnnecessaryCommasCheckbox!!.isSelected
        config!!.removeUnnecessaryLineBreaksAfterArrows = removeUnnecessaryLineBreaksAfterArrowsCheckbox!!.isSelected

        config!!.indentationIsEnabled = indentationIsEnabledCheckbox!!.isSelected
    }

    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(removeUnnecessaryCommasCheckbox) })
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(removeUnnecessaryLineBreaksAfterArrowsCheckbox) })
            .addComponent(JPanel(FlowLayout(FlowLayout.LEFT)).also { it.add(indentationIsEnabledCheckbox) })

        return JPanel(BorderLayout()).also { it.add(formBuilder.panel, BorderLayout.NORTH) }
    }

    override fun dispose()
    {
        removeUnnecessaryCommasCheckbox = null
        removeUnnecessaryLineBreaksAfterArrowsCheckbox = null

        indentationIsEnabledCheckbox = null
    }

    override fun getDisplayName(): String = "DartFormat"

    override fun isModified(): Boolean
    {
        if (config == null)
        {
            println("Error in isModified: configState == null")
            return false
        }

        return config!!.removeUnnecessaryCommas != removeUnnecessaryCommasCheckbox!!.isSelected
                || config!!.removeUnnecessaryLineBreaksAfterArrows != removeUnnecessaryLineBreaksAfterArrowsCheckbox!!.isSelected
                || config!!.indentationIsEnabled != indentationIsEnabledCheckbox!!.isSelected
    }

    override fun reset()
    {
        if (config == null)
        {
            println("Error in reset: configState == null")
            return
        }

        removeUnnecessaryCommasCheckbox!!.isSelected = config!!.removeUnnecessaryCommas
        removeUnnecessaryLineBreaksAfterArrowsCheckbox!!.isSelected = config!!.removeUnnecessaryLineBreaksAfterArrows

        indentationIsEnabledCheckbox!!.isSelected = config!!.indentationIsEnabled
    }
}
