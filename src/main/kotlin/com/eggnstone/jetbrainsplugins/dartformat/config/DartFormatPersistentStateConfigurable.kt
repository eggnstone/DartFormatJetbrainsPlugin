package com.eggnstone.jetbrainsplugins.dartformat.config

import com.eggnstone.jetbrainsplugins.dartformat.dotlin.DotlinLogger
import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.text.NumberFormat
import javax.swing.*
import javax.swing.text.NumberFormatter

class DartFormatPersistentStateConfigurable : Configurable, Disposable
{
    private val config: DartFormatConfig? get() = DartFormatPersistentStateComponent.instance?.state

    private var removeUnnecessaryCommasCheckbox: JCheckBox? = JCheckBox("Remove unnecessary commas")

    private var removeLineBreaksAfterArrowsCheckbox: JCheckBox? = JCheckBox("Remove line breaks after arrows")

    private var indentationIsEnabledCheckbox: JCheckBox? = JCheckBox("Indent")
    private val indentationSpacesPerLevelFormatter = NumberFormatter(NumberFormat.getIntegerInstance())
        .also {
            it.minimum = 1
            it.maximum = 8
            it.allowsInvalid = false
        }

    private var indentationSpacesPerLevelField: JFormattedTextField? =
        JFormattedTextField(indentationSpacesPerLevelFormatter)
            .also { it.text = config!!.indentationSpacesPerLevel.toString() }

    override fun apply()
    {
        if (config == null)
        {
            DotlinLogger.log("Error in apply: configState == null")
            return
        }

        config!!.removeUnnecessaryCommas = removeUnnecessaryCommasCheckbox!!.isSelected

        config!!.removeLineBreaksAfterArrows = removeLineBreaksAfterArrowsCheckbox!!.isSelected

        config!!.indentationIsEnabled = indentationIsEnabledCheckbox!!.isSelected

        indentationSpacesPerLevelField!!.text.toIntOrNull()?.let {
            if (it in 1..8)
                config!!.indentationSpacesPerLevel = it
        }
    }

    @Suppress("JoinDeclarationAndAssignment")
    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()

        formBuilder.addComponent(createRemovalsPanel())
        formBuilder.addComponent(createLineBreaksPanel())
        formBuilder.addComponent(createIndentationPanel())

        val panel = JPanel(BorderLayout())
        panel.add(formBuilder.panel, BorderLayout.NORTH)

        return panel
    }

    private fun createIndentationPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(indentationIsEnabledCheckbox)
        panel.add(indentationSpacesPerLevelField)
        panel.add(JLabel("spaces"))

        return panel
    }

    private fun createLineBreaksPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(removeLineBreaksAfterArrowsCheckbox)

        return panel
    }

    private fun createRemovalsPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(removeUnnecessaryCommasCheckbox)

        return panel
    }

    override fun dispose()
    {
        removeUnnecessaryCommasCheckbox = null

        removeLineBreaksAfterArrowsCheckbox = null

        indentationIsEnabledCheckbox = null
        indentationSpacesPerLevelField = null
    }

    override fun getDisplayName(): String = "DartFormat"

    override fun isModified(): Boolean
    {
        if (config == null)
        {
            DotlinLogger.log("Error in isModified: configState == null")
            return false
        }

        return config!!.removeUnnecessaryCommas != removeUnnecessaryCommasCheckbox!!.isSelected
        || config!!.removeLineBreaksAfterArrows != removeLineBreaksAfterArrowsCheckbox!!.isSelected
        || config!!.indentationIsEnabled != indentationIsEnabledCheckbox!!.isSelected
        || config!!.indentationSpacesPerLevel != indentationSpacesPerLevelField!!.text.toIntOrNull()
    }

    override fun reset()
    {
        if (config == null)
        {
            DotlinLogger.log("Error in reset: configState == null")
            return
        }

        removeUnnecessaryCommasCheckbox!!.isSelected = config!!.removeUnnecessaryCommas

        removeLineBreaksAfterArrowsCheckbox!!.isSelected = config!!.removeLineBreaksAfterArrows

        indentationIsEnabledCheckbox!!.isSelected = config!!.indentationIsEnabled
        indentationSpacesPerLevelField!!.text = config!!.indentationSpacesPerLevel.toString()
    }
}
