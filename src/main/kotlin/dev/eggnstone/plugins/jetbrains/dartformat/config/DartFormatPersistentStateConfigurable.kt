package dev.eggnstone.plugins.jetbrains.dartformat.config

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.ui.JBColor
import com.intellij.util.ui.FormBuilder
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import java.awt.BorderLayout
import java.awt.Cursor
import java.awt.Dimension
import java.awt.FlowLayout
import java.net.URI
import java.text.NumberFormat
import javax.swing.*
import javax.swing.text.NumberFormatter

class DartFormatPersistentStateConfigurable : Configurable, Disposable
{
    private val config: DartFormatConfig get() = DartFormatConfigGetter.get()

    private var addNewLineAfterClosingBraceCheckbox = JCheckBox("Add new line after closing brace")
    private var addNewLineAfterOpeningBraceCheckbox = JCheckBox("Add new line after opening brace")
    private var addNewLineAfterSemicolonCheckbox = JCheckBox("Add new line after semicolon")
    private var addNewLineAtEndOfTextCheckbox = JCheckBox("Add new line at the end of the file")
    private var addNewLineBeforeClosingBraceCheckbox = JCheckBox("Add new line before closing brace")
    private var addNewLineBeforeOpeningBraceCheckbox = JCheckBox("Add new line before opening brace")
    private var fixSpacesCheckbox = JCheckBox("Fix spaces")

    private var fontFamily = fixSpacesCheckbox.font.family
    private var fontSize = fixSpacesCheckbox.font.size
    private var codeBackground = if (fixSpacesCheckbox.background.red == 0) "#666" else if (fixSpacesCheckbox.background.red < 128) "#000" else "#fff"

    private var fixSpacesTextPane = JTextPane()
        .also {
            it.contentType = "text/html"
            it.text = "<html><body>" +
                "<div style='font-family: " + fontFamily + "; font-size: " + fontSize + "pt;'>" +
                //"Examples:" +
                //"<br/>Red: " + fixSpacesCheckbox.background.red + ", Green: " + fixSpacesCheckbox.background.green + ", Blue: " + fixSpacesCheckbox.background.blue + "<br/>" +
                //"<div style='margin-top: 4px;'>" +
                //"For example <code style='font-size: 12pt; background: " + codeBackground + ";'>if(a&lt;b)</code>" +
                //" > " +
                //"<code style='font-size: 12pt; background: " + codeBackground + ";'>if (a &lt; b)</code>" +
                //"</div>" +
                //"<div style='margin-top: 4px;'>" +
                "For example &nbsp; <code style='font-size: 12pt; background: " + codeBackground + ";'>for(int i=0;i&lt;10;i++)</code>" +
                " &nbsp; > &nbsp; " +
                "<code style='font-size: 12pt; background: " + codeBackground + ";'>for (int i = 0; i &lt; 10; i++)</code>" +
                //"</div>" +
                "</div>" +
                "</body></html>"
        }

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
        config.addNewLineAfterClosingBrace = addNewLineAfterClosingBraceCheckbox.isSelected
        config.addNewLineAfterOpeningBrace = addNewLineAfterOpeningBraceCheckbox.isSelected
        config.addNewLineAfterSemicolon = addNewLineAfterSemicolonCheckbox.isSelected
        config.addNewLineAtEndOfText = addNewLineAtEndOfTextCheckbox.isSelected
        config.addNewLineBeforeClosingBrace = addNewLineBeforeClosingBraceCheckbox.isSelected
        config.addNewLineBeforeOpeningBrace = addNewLineBeforeOpeningBraceCheckbox.isSelected
        config.fixSpaces = fixSpacesCheckbox.isSelected

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

    private fun createPanelAndAdd(jComponent: JComponent): JPanel = createPanelFlowLayoutLeading().apply { add(jComponent) }

    override fun createComponent(): JComponent
    {
        val formBuilder: FormBuilder = FormBuilder.createFormBuilder()

        @Suppress("JoinDeclarationAndAssignment")
        var sectionPanel: JPanel

        /*sectionPanel = createAndAddSectionPanel("General", formBuilder)
        sectionPanel.add(createPanelAndAdd(createIntroPanel()))*/

        sectionPanel = createAndAddSectionPanel("Line Breaks", formBuilder)
        sectionPanel.add(createPanelAndAdd(addNewLineBeforeOpeningBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterOpeningBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineBeforeClosingBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterClosingBraceCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAfterSemicolonCheckbox))
        sectionPanel.add(createPanelAndAdd(addNewLineAtEndOfTextCheckbox))

        sectionPanel = createAndAddSectionPanel("Spaces", formBuilder)
        sectionPanel.add(createPanelAndAdd(fixSpacesCheckbox))
        sectionPanel.add(createIndentedPanelAndAdd(25, fixSpacesTextPane))

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

    @Suppress("SameParameterValue")
    private fun createHtmlLabel(s: String/*, preferredWidth: Int*/): JLabel
    {
        val label = JLabel("<html><body>$s</body></html>")

        if (Constants.DEBUG_SETTINGS_DIALOG)
            label.border = BorderFactory.createMatteBorder(1, 1, 1, 1, JBColor.RED)

        return label
    }

    private fun createIntroPanel(): JPanel
    {
        val panel = createPanelBoxLayoutYAxis()

        val htmlLabel = createHtmlLabel(
            "This plugin is a wrapper around the <code>dart_format</code> package on <code>pub.dev</code>.<br/>" +
                "Please follow the install instruction there.<br/>" +
                "Basically just execute this:<pre>dart pub global activate dart_format</pre>"
        )
        panel.add(htmlLabel)

        panel.add(JPanel())
        panel.add(createLink("<code>dart_format</code> package on <code>pub.dev</code>", "https://pub.dev/packages/dart_format"))
        panel.add(JPanel())
        panel.add(createLink("Installation instructions on <code>pub.dev</code>", "https://pub.dev/packages/dart_format/install"))

        // htmlLabel's width is not properly set, so we set it manually.
        htmlLabel.preferredSize = Dimension(panel.preferredSize.width, htmlLabel.preferredSize.height)

        return panel
    }

    private fun createLink(name: String, url: String): JLabel
    {
        val label = JLabel("<html><body><a href=\"$url\">$name</a></body></html>")
        label.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        label.addMouseListener(OpenUrlAction(URI.create(url)))
        return label
    }

    private fun createAndAddSectionPanel(name: String, formBuilder: FormBuilder): JPanel
    {
        val panel = createPanelBoxLayoutYAxis()

        if (Constants.DEBUG_SETTINGS_DIALOG)
            panel.border = BorderFactory.createMatteBorder(4, 12, 20, 0, JBColor.RED)
        else
            panel.border = BorderFactory.createEmptyBorder(4, 12, 20, 0)

        formBuilder.addComponent(createSection(name, panel))
        return panel
    }

    private fun createIndentedPanelAndAdd(indent: Int, jComponent: JComponent): JPanel
    {
        val panel = createPanelBoxLayoutYAxis()

        if (Constants.DEBUG_SETTINGS_DIALOG)
            panel.border = BorderFactory.createMatteBorder(0, indent, 0, 0, JBColor.GREEN)
        else
            panel.border = BorderFactory.createEmptyBorder(0, indent, 0, 0)

        panel.add(jComponent)
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
        return config.addNewLineAfterClosingBrace != addNewLineAfterClosingBraceCheckbox.isSelected
            || config.addNewLineAfterOpeningBrace != addNewLineAfterOpeningBraceCheckbox.isSelected
            || config.addNewLineAfterSemicolon != addNewLineAfterSemicolonCheckbox.isSelected
            || config.addNewLineAtEndOfText != addNewLineAtEndOfTextCheckbox.isSelected
            || config.addNewLineBeforeClosingBrace != addNewLineBeforeClosingBraceCheckbox.isSelected
            || config.addNewLineBeforeOpeningBrace != addNewLineBeforeOpeningBraceCheckbox.isSelected
            || config.fixSpaces != fixSpacesCheckbox.isSelected
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
        addNewLineAfterClosingBraceCheckbox.isSelected = config.addNewLineAfterClosingBrace
        addNewLineAfterOpeningBraceCheckbox.isSelected = config.addNewLineAfterOpeningBrace
        addNewLineAfterSemicolonCheckbox.isSelected = config.addNewLineAfterSemicolon
        addNewLineAtEndOfTextCheckbox.isSelected = config.addNewLineAtEndOfText
        addNewLineBeforeClosingBraceCheckbox.isSelected = config.addNewLineBeforeClosingBrace
        addNewLineBeforeOpeningBraceCheckbox.isSelected = config.addNewLineBeforeOpeningBrace
        fixSpacesCheckbox.isSelected = config.fixSpaces
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
