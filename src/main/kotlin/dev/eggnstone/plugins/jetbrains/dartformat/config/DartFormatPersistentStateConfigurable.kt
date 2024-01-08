package dev.eggnstone.plugins.jetbrains.dartformat.config

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
    private val config: DartFormatConfig get() = DartFormatPersistentStateComponent.instance?.state ?: DartFormatConfig()

    private var addNewLineAfterClosingBraceCheckbox: JCheckBox = JCheckBox("Add new line after closing brace")
    private var addNewLineAfterOpeningBraceCheckbox: JCheckBox = JCheckBox("Add new line after opening brace")
    private var addNewLineAfterSemicolonCheckbox: JCheckBox = JCheckBox("Add new line after semicolon")
    private var addNewLineAtEndOfTextCheckbox: JCheckBox = JCheckBox("Add new line at the end of the text")
    private var addNewLineBeforeClosingBraceCheckbox: JCheckBox = JCheckBox("Add new line before closing brace")
    private var addNewLineBeforeOpeningBraceCheckbox: JCheckBox = JCheckBox("Add new line before opening brace")

    private var indentationIsEnabledCheckbox: JCheckBox = JCheckBox("Indent")
    private val indentationSpacesPerLevelFormatter = NumberFormatter(NumberFormat.getIntegerInstance())
        .also {
            it.minimum = 1
            it.maximum = 8
            it.allowsInvalid = false
        }

    private var indentationSpacesPerLevelField: JFormattedTextField =
        JFormattedTextField(indentationSpacesPerLevelFormatter)
            .also { it.text = config.indentationSpacesPerLevel.toString() }

    private var maxEmptyLinesIsEnabledCheckbox: JCheckBox = JCheckBox("Max empty lines:")
    private val maxEmptyLinesFormatter = NumberFormatter(NumberFormat.getIntegerInstance())
        .also {
            it.minimum = 1
            it.maximum = 4
            it.allowsInvalid = false
        }

    private var maxEmptyLinesField: JFormattedTextField =
        JFormattedTextField(maxEmptyLinesFormatter)
            .also { it.text = config.maxEmptyLines.toString() }

    //private var removeUnnecessaryCommasCheckbox: JCheckBox = JCheckBox("Remove unnecessary commas")
    //private var removeLineBreaksAfterArrowsCheckbox: JCheckBox = JCheckBox("Remove line breaks after arrows")

    override fun apply()
    {
        config.addNewLineAfterOpeningBrace = addNewLineAfterOpeningBraceCheckbox.isSelected
        config.addNewLineAfterClosingBrace = addNewLineAfterClosingBraceCheckbox.isSelected
        config.addNewLineBeforeOpeningBrace = addNewLineBeforeOpeningBraceCheckbox.isSelected
        config.addNewLineBeforeClosingBrace = addNewLineBeforeClosingBraceCheckbox.isSelected
        config.addNewLineAfterSemicolon = addNewLineAfterSemicolonCheckbox.isSelected
        config.addNewLineAtEndOfText = addNewLineAtEndOfTextCheckbox.isSelected

        config.indentationIsEnabled = indentationIsEnabledCheckbox.isSelected
        indentationSpacesPerLevelField.text.toIntOrNull()?.let {
            if (it in 1..8)
                config.indentationSpacesPerLevel = it
        }

        config.maxEmptyLinesIsEnabled = maxEmptyLinesIsEnabledCheckbox.isSelected
        maxEmptyLinesField.text.toIntOrNull()?.let {
            if (it in 1..4)
                config.maxEmptyLines = it
        }

        //config.removeUnnecessaryCommas = removeUnnecessaryCommasCheckbox.isSelected
        //config.removeLineBreaksAfterArrows = removeLineBreaksAfterArrowsCheckbox.isSelected
    }

    private fun createAddNewLineAfterClosingBracePanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(addNewLineAfterClosingBraceCheckbox)

        return panel
    }

    private fun createAddNewLineAfterOpeningBracePanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(addNewLineAfterOpeningBraceCheckbox)

        return panel
    }

    private fun createAddNewLineAfterSemicolonPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(addNewLineAfterSemicolonCheckbox)

        return panel
    }

    private fun createAddNewLineBeforeClosingBracePanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(addNewLineBeforeClosingBraceCheckbox)

        return panel
    }

    private fun createAddNewLineBeforeOpeningBracePanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(addNewLineBeforeOpeningBraceCheckbox)

        return panel
    }

    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()

        formBuilder.addComponent(createAddNewLineBeforeOpeningBracePanel())
        formBuilder.addComponent(createAddNewLineAfterOpeningBracePanel())
        formBuilder.addComponent(createAddNewLineBeforeClosingBracePanel())
        formBuilder.addComponent(createAddNewLineAfterClosingBracePanel())
        formBuilder.addComponent(createAddNewLineAfterSemicolonPanel())

        //formBuilder.addComponent(createRemovalsPanel())
        //formBuilder.addComponent(createLineBreaksPanel())

        formBuilder.addComponent(createIndentationPanel())

        formBuilder.addComponent(createMaxLinesPanel())

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

        //panel.add(removeLineBreaksAfterArrowsCheckbox)

        return panel
    }

    private fun createMaxLinesPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        panel.add(maxEmptyLinesIsEnabledCheckbox)
        panel.add(maxEmptyLinesField)

        return panel
    }

    private fun createRemovalsPanel(): JComponent
    {
        val panel = JPanel(FlowLayout(FlowLayout.LEADING))

        //panel.add(removeUnnecessaryCommasCheckbox)
        //panel.add(removeUnnecessaryCommasCheckbox)

        return panel
    }

    override fun dispose()
    {
    }

    override fun getDisplayName(): String = "DartFormat"

    override fun isModified(): Boolean
    {
        return config.addNewLineAfterOpeningBrace != addNewLineAfterOpeningBraceCheckbox.isSelected
        || config.addNewLineAfterClosingBrace != addNewLineAfterClosingBraceCheckbox.isSelected
        || config.addNewLineBeforeOpeningBrace != addNewLineBeforeOpeningBraceCheckbox.isSelected
        || config.addNewLineBeforeClosingBrace != addNewLineBeforeClosingBraceCheckbox.isSelected
        || config.addNewLineAfterSemicolon != addNewLineAfterSemicolonCheckbox.isSelected
        || config.addNewLineAtEndOfText != addNewLineAtEndOfTextCheckbox.isSelected
        || config.indentationIsEnabled != indentationIsEnabledCheckbox.isSelected
        || config.indentationSpacesPerLevel != indentationSpacesPerLevelField.text.toIntOrNull()
        || config.maxEmptyLines != maxEmptyLinesField.text.toIntOrNull()
        || config.maxEmptyLinesIsEnabled != maxEmptyLinesIsEnabledCheckbox.isSelected
        /*|| config.removeUnnecessaryCommas != removeUnnecessaryCommasCheckbox.isSelected
        || config.removeLineBreaksAfterArrows != removeLineBreaksAfterArrowsCheckbox.isSelected*/
    }

    override fun reset()
    {
        addNewLineAfterOpeningBraceCheckbox.isSelected = config.addNewLineAfterOpeningBrace
        addNewLineAfterClosingBraceCheckbox.isSelected = config.addNewLineAfterClosingBrace
        addNewLineBeforeOpeningBraceCheckbox.isSelected = config.addNewLineBeforeOpeningBrace
        addNewLineBeforeClosingBraceCheckbox.isSelected = config.addNewLineBeforeClosingBrace
        addNewLineAfterSemicolonCheckbox.isSelected = config.addNewLineAfterSemicolon
        addNewLineAtEndOfTextCheckbox.isSelected = config.addNewLineAtEndOfText

        indentationIsEnabledCheckbox.isSelected = config.indentationIsEnabled
        indentationSpacesPerLevelField.text = config.indentationSpacesPerLevel.toString()

        maxEmptyLinesField.text = config.maxEmptyLines.toString()
        maxEmptyLinesIsEnabledCheckbox.isSelected = config.maxEmptyLinesIsEnabled

        /*removeUnnecessaryCommasCheckbox.isSelected = config.removeUnnecessaryCommas
        removeLineBreaksAfterArrowsCheckbox.isSelected = config.removeLineBreaksAfterArrows*/
    }
}
