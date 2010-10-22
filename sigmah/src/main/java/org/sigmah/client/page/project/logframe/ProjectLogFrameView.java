package org.sigmah.client.page.project.logframe;

import org.sigmah.client.i18n.I18N;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * The view manipulated by the log frame presenter.
 * 
 * @author tmi
 * 
 */
public class ProjectLogFrameView extends ProjectLogFramePresenter.View {

    // Toolbar buttons.
    private Button saveButton;
    private Button copyButton;
    private Button pasteButton;
    private Button wordButton;
    private Button excelButton;

    // Textboxes.
    private TextField<String> titleTextBox;
    private TextField<String> mainObjectiveTextBox;

    // Grid.
    private final ProjectLogFrameGrid logFrameGrid;

    /**
     * Builds the log frame main component.
     * 
     * @return The log frame main component.
     */
    public ProjectLogFrameView() {

        // Title panel.
        final ContentPanel titlePanel = buildTitlePanel();

        // Main objective panel.
        final ContentPanel mainObjectivePanel = buildMainObjectivePanel();

        // Flex table.
        logFrameGrid = new ProjectLogFrameGrid();

        // Toolbar.
        final ToolBar toolBar = buildToolbar();

        // Log frame main panel.
        final ContentPanel mainPanel = buildMainPanel();

        mainPanel.setTopComponent(toolBar);
        mainPanel.add(titlePanel, new VBoxLayoutData(new Margins(4, 8, 0, 8)));
        mainPanel.add(mainObjectivePanel, new VBoxLayoutData(new Margins(4, 8, 4, 8)));
        mainPanel.add(logFrameGrid.getWidget(), new VBoxLayoutData(new Margins(0, 8, 0, 8)));

        // Adds the main panel.
        setLayout(new FitLayout());
        add(mainPanel);
    }

    /**
     * Builds the log frame title panel.
     * 
     * @return The title panel.
     */
    private ContentPanel buildTitlePanel() {

        // Title label.
        final Label titleLabel = new Label(I18N.CONSTANTS.logFrameActionTitle());
        titleLabel.addStyleName("flexibility-element-label");
        titleLabel.setLabelFor("logFrameTitleBox-input");
        titleLabel.setWidth(100);

        // Title box.
        titleTextBox = new TextField<String>();
        titleTextBox.addStyleName("flexibility-text-field");
        titleTextBox.setId("logFrameTitleBox");

        // Title panel.
        final ContentPanel titlePanel = new ContentPanel();
        titlePanel.setBodyBorder(false);
        titlePanel.setHeaderVisible(false);
        titlePanel.setLayout(new HBoxLayout());

        titlePanel.add(titleLabel, new HBoxLayoutData(new Margins(4, 0, 0, 0)));
        final HBoxLayoutData flex = new HBoxLayoutData(new Margins(0, 0, 0, 5));
        flex.setFlex(1);
        titlePanel.add(titleTextBox, flex);

        return titlePanel;
    }

    /**
     * Builds the log frame main objective panel.
     * 
     * @return The main objective panel.
     */
    private ContentPanel buildMainObjectivePanel() {

        // Main objective label.
        final Label mainObjectiveLabel = new Label(I18N.CONSTANTS.logFrameMainObjective());
        mainObjectiveLabel.addStyleName("flexibility-element-label");
        mainObjectiveLabel.setLabelFor("logFrameMainObjectiveBox-input");
        mainObjectiveLabel.setWidth(100);

        // Main objective box.
        mainObjectiveTextBox = new TextField<String>();
        mainObjectiveTextBox.addStyleName("flexibility-text-field");
        mainObjectiveTextBox.setId("logFrameMainObjectiveBox");

        // Main objective panel.
        final ContentPanel mainObjectivePanel = new ContentPanel();
        mainObjectivePanel.setBodyBorder(false);
        mainObjectivePanel.setHeaderVisible(false);
        mainObjectivePanel.setLayout(new HBoxLayout());

        mainObjectivePanel.add(mainObjectiveLabel, new HBoxLayoutData(new Margins(4, 0, 0, 0)));
        final HBoxLayoutData flex2 = new HBoxLayoutData(new Margins(0, 0, 0, 5));
        flex2.setFlex(1);
        mainObjectivePanel.add(mainObjectiveTextBox, flex2);

        return mainObjectivePanel;
    }

    /**
     * Builds the actions toolbar.
     * 
     * @return The actions toolbar.
     */
    private ToolBar buildToolbar() {

        // Save button.
        saveButton = new Button(I18N.CONSTANTS.save());
        saveButton.setEnabled(false);

        // Copy button.
        copyButton = new Button(I18N.CONSTANTS.copy());
        copyButton.setEnabled(true);
        //TODO unmask it.
        copyButton.setVisible(false);
        
        // Paste button.
        pasteButton = new Button(I18N.CONSTANTS.paste());
        pasteButton.setEnabled(true);
        //TODO unmask it.
        pasteButton.setVisible(false);
        
        // Export to Word button.
        wordButton = new Button(I18N.CONSTANTS.exportToWord());
        wordButton.setEnabled(true);

        // Export to Excel button.
        excelButton = new Button(I18N.CONSTANTS.exportToExcel());
        excelButton.setEnabled(false);

        // Actions toolbar.
        final ToolBar toolBar = new ToolBar();
        toolBar.setAlignment(HorizontalAlignment.RIGHT);
        toolBar.setBorders(false);

        toolBar.add(saveButton);
        toolBar.add(copyButton);
        toolBar.add(pasteButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(wordButton);
        toolBar.add(excelButton);

        return toolBar;
    }

    /**
     * Builds the view main panel.
     * 
     * @return The view main panel.
     */
    private ContentPanel buildMainPanel() {

        final ContentPanel mainPanel = new ContentPanel();
        final VBoxLayout layout = new VBoxLayout();
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        mainPanel.setLayout(layout);
        mainPanel.setHeaderVisible(true);
        mainPanel.setBorders(true);
        mainPanel.setHeading(I18N.CONSTANTS.logFrame());
        mainPanel.setWidth("100%");

        mainPanel.addStyleName("logframe-grid-main-panel");

        return mainPanel;
    }

    @Override
    public ProjectLogFrameGrid getLogFrameGrid() {
        return logFrameGrid;
    }

    @Override
    public Button getSaveButton() {
        return saveButton;
    }

    @Override
    public Button getCopyButton() {
        return copyButton;
    }

    @Override
    public Button getPasteButton() {
        return pasteButton;
    }

    @Override
    public Button getWordExportButton() {
        return wordButton;
    }

    @Override
    public Button getExcelExportButton() {
        return excelButton;
    }

    @Override
    public TextField<String> getLogFrameTitleTextBox() {
        return titleTextBox;
    }

    @Override
    public TextField<String> getLogFrameMainObjectiveTextBox() {
        return mainObjectiveTextBox;
    }
}
