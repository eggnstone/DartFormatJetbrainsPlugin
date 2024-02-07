package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.util.ui.FormBuilder
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.FlowLayout
import java.net.URI
import java.text.NumberFormat
import javax.swing.*
import javax.swing.text.NumberFormatter

class DartFormatPersistentStateConfigurable : Configurable, Disposable
{
    private val config: DartFormatConfig get() = DartFormatPersistentStateComponent.instance?.state ?: DartFormatConfig()

    private var acceptBetaCheckbox = JCheckBox(//"<html><body>" +
        "I accept that this is a beta version and not everything works as it should.<br/>" +
        "I will be patient with the developer. :)" //+
        //"</body></html>"
    )

    private var addNewLineAfterClosingBraceCheckbox = JCheckBox("Add new line after closing brace")
    private var addNewLineAfterOpeningBraceCheckbox = JCheckBox("Add new line after opening brace")
    private var addNewLineAfterSemicolonCheckbox = JCheckBox("Add new line after semicolon")
    private var addNewLineAtEndOfTextCheckbox = JCheckBox("Add new line at the end of the text")
    private var addNewLineBeforeClosingBraceCheckbox = JCheckBox("Add new line before closing brace")
    private var addNewLineBeforeOpeningBraceCheckbox = JCheckBox("Add new line before opening brace")

    private var indentationIsEnabledCheckbox = JCheckBox("Indent")
    private val indentationSpacesPerLevelFormatter = NumberFormatter(NumberFormat.getIntegerInstance())
        .also {
            it.minimum = 1
            it.maximum = 8
            it.allowsInvalid = false
        }

    private var indentationSpacesPerLevelField: JFormattedTextField =
        JFormattedTextField(indentationSpacesPerLevelFormatter)
            .also { it.text = config.indentationSpacesPerLevel.toString() }

    private var maxEmptyLinesIsEnabledCheckbox = JCheckBox("Max empty lines:")
    private val maxEmptyLinesFormatter = NumberFormatter(NumberFormat.getIntegerInstance())
        .also {
            it.minimum = 1
            it.maximum = 4
            it.allowsInvalid = false
        }

    private var maxEmptyLinesField: JFormattedTextField =
        JFormattedTextField(maxEmptyLinesFormatter)
            .also { it.text = config.maxEmptyLines.toString() }

    private var removeTrailingCommasCheckbox = JCheckBox("Remove trailing commas")
    //private var removeLineBreaksAfterArrowsCheckbox = JCheckBox("Remove line breaks after arrows")

    override fun apply()
    {
        @Suppress("DuplicatedCode")
        config.acceptBeta = acceptBetaCheckbox.isSelected

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

        config.removeTrailingCommas = removeTrailingCommasCheckbox.isSelected
        //config.removeLineBreaksAfterArrows = removeLineBreaksAfterArrowsCheckbox.isSelected
    }

    private fun createPanelAndAdd(checkbox: JComponent): JPanel = createPanelFlowLayoutLeading().apply { add(checkbox) }

    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()

        @Suppress("JoinDeclarationAndAssignment")
        var sectionPanel: JPanel

        sectionPanel = createAndAddSectionPanel("General", formBuilder)
        sectionPanel.add(createPanelAndAdd(acceptBetaCheckbox))
        sectionPanel.add(createPanelAndAdd(createIntroLabel()))

        sectionPanel = createAndAddSectionPanel("Line Breaks", formBuilder)
        sectionPanel.add(createPanelAndAdd(addNewLineBeforeOpeningBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterOpeningBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineBeforeClosingBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterClosingBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterSemicolonCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAtEndOfTextCheckbox))

        sectionPanel = createAndAddSectionPanel("Removals", formBuilder)
        sectionPanel.add(createPanelAndAdd(removeTrailingCommasCheckbox))

        sectionPanel = createAndAddSectionPanel("Indentation", formBuilder)
        sectionPanel.add(createIndentationPanel())

        sectionPanel = createAndAddSectionPanel("Empty Lines", formBuilder)
        sectionPanel.add(createMaxLinesPanel())

        val finalPanel = JPanel(BorderLayout())
        finalPanel.add(formBuilder.panel, BorderLayout.NORTH)

        return finalPanel
    }

    private fun createIntroLabel(): JLabel
    {
        val label = JLabel(//"<html><body>" +
            "This plugin is a wrapper around my <a href=\"https://pub.dev/packages/dart_format\">dart_format</a> package on pub.dev.<br/>" +
            "Please follow the <a href=\"https://pub.dev/packages/dart_format/install\">install instruction</a> there." //+
            //"</body></html>"
        )
        // TODO:
        // - only open on links
        // - display different cursor
        label.addMouseListener(OpenUrlAction(URI.create("https://pub.dev/packages/dart_format/install")))
        return label
    }

    private fun createAndAddSectionPanel(name: String, formBuilder: FormBuilder): JPanel
    {
        val panel = createPanelBoxLayoutYAxis()
        //panel.border = BorderFactory.createMatteBorder(4, 12, 20, 0, JBColor.RED)
        panel.border = BorderFactory.createEmptyBorder(4, 12, 20, 0)
        formBuilder.addComponent(createSection(name, panel))
        return panel
    }

    private fun createSection(name: String, innerPanel: JPanel): JPanel
    {
        val panel = createPanelBoxLayoutYAxis()
        panel.add(createSectionHead(name))
        panel.add(innerPanel)
        return panel
    }

    private fun createPanelBoxLayoutXAxis(): JPanel
    {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        return panel
    }

    private fun createPanelBoxLayoutYAxis(): JPanel
    {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        return panel
    }

    private fun createPanelFlowLayoutLeading(): JPanel
    {
        return JPanel(FlowLayout(FlowLayout.LEADING))
    }

    @Suppress("SameParameterValue")
    private fun createHorizontalSpacer(width: Int): JPanel
    {
        val panel = JPanel()
        panel.preferredSize = Dimension(width, 0)
        return panel
    }

    private fun createHorizontalSeparator(): JSeparator
    {
        val separator = JSeparator(SwingConstants.HORIZONTAL)
        separator.maximumSize = Dimension(Int.MAX_VALUE, 2)
        return separator
    }

    private fun createSectionHead(name: String): JPanel
    {
        val panel = createPanelBoxLayoutXAxis()
        panel.add(JLabel(name))
        panel.add(createHorizontalSpacer(6))
        panel.add(createHorizontalSeparator())
        return panel
    }

    private fun createIndentationPanel(): JPanel
    {
        val panel = createPanelFlowLayoutLeading()
        panel.add(indentationIsEnabledCheckbox)
        panel.add(indentationSpacesPerLevelField)
        panel.add(JLabel("spaces"))
        return panel
    }

    private fun createMaxLinesPanel(): JPanel
    {
        val panel = createPanelFlowLayoutLeading()
        panel.add(maxEmptyLinesIsEnabledCheckbox)
        panel.add(maxEmptyLinesField)
        return panel
    }

    // Is required although empty
    override fun dispose()
    {
    }

    override fun getDisplayName(): String = "DartFormat"

    override fun isModified(): Boolean
    {
        return config.acceptBeta != acceptBetaCheckbox.isSelected
            || config.addNewLineAfterClosingBrace != addNewLineAfterClosingBraceCheckbox.isSelected
            || config.addNewLineAfterOpeningBrace != addNewLineAfterOpeningBraceCheckbox.isSelected
            || config.addNewLineAfterSemicolon != addNewLineAfterSemicolonCheckbox.isSelected
            || config.addNewLineAtEndOfText != addNewLineAtEndOfTextCheckbox.isSelected
            || config.addNewLineBeforeClosingBrace != addNewLineBeforeClosingBraceCheckbox.isSelected
            || config.addNewLineBeforeOpeningBrace != addNewLineBeforeOpeningBraceCheckbox.isSelected
            || config.indentationIsEnabled != indentationIsEnabledCheckbox.isSelected
            || config.indentationSpacesPerLevel != indentationSpacesPerLevelField.text.toIntOrNull()
            || config.maxEmptyLines != maxEmptyLinesField.text.toIntOrNull()
            || config.maxEmptyLinesIsEnabled != maxEmptyLinesIsEnabledCheckbox.isSelected
            || config.removeTrailingCommas != removeTrailingCommasCheckbox.isSelected
        /*
        || config.removeLineBreaksAfterArrows != removeLineBreaksAfterArrowsCheckbox.isSelected
        */
    }

    override fun reset()
    {
        @Suppress("DuplicatedCode")
        acceptBetaCheckbox.isSelected = config.acceptBeta
        addNewLineAfterClosingBraceCheckbox.isSelected = config.addNewLineAfterClosingBrace
        addNewLineAfterOpeningBraceCheckbox.isSelected = config.addNewLineAfterOpeningBrace
        addNewLineAfterSemicolonCheckbox.isSelected = config.addNewLineAfterSemicolon
        addNewLineAtEndOfTextCheckbox.isSelected = config.addNewLineAtEndOfText
        addNewLineBeforeClosingBraceCheckbox.isSelected = config.addNewLineBeforeClosingBrace
        addNewLineBeforeOpeningBraceCheckbox.isSelected = config.addNewLineBeforeOpeningBrace
        indentationIsEnabledCheckbox.isSelected = config.indentationIsEnabled
        indentationSpacesPerLevelField.text = config.indentationSpacesPerLevel.toString()
        maxEmptyLinesField.text = config.maxEmptyLines.toString()
        maxEmptyLinesIsEnabledCheckbox.isSelected = config.maxEmptyLinesIsEnabled
        removeTrailingCommasCheckbox.isSelected = config.removeTrailingCommas
        /*
        removeLineBreaksAfterArrowsCheckbox.isSelected = config.removeLineBreaksAfterArrows
        */
    }
}
