/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.reports;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.page.project.reports.images.ToolbarImages;
import org.sigmah.client.ui.FoldPanel;
import org.sigmah.shared.command.GetProjectReports;
import org.sigmah.shared.dto.report.ProjectReportDTO;
import org.sigmah.shared.dto.report.ProjectReportSectionDTO;
import org.sigmah.shared.dto.report.RichTextElementDTO;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectReportsView extends LayoutContainer {

    private EventBus eventBus;
    private ProjectState currentState;
    private LayoutContainer mainPanel;
    private RichTextArea.Formatter currentFormatter;

    public ProjectReportsView(EventBus eventBus, ListStore<GetProjectReports.ReportReference> store) {
        this.eventBus = eventBus;
        final BorderLayout layout = new BorderLayout();
        layout.setContainerStyle("x-border-layout-ct main-background"); // Adds a dark background between objects managed by this layout
        setLayout(layout);

        BorderLayoutData layoutData;

        final ContentPanel documentList = createDocumentList(store);
        layoutData = new BorderLayoutData(LayoutRegion.WEST, 400);
        layoutData.setCollapsible(true);
        add(documentList, layoutData);

        mainPanel = createMainPanel();
        layoutData = new BorderLayoutData(LayoutRegion.CENTER);
        layoutData.setMargins(new Margins(0, 0, 0, 8));
        add(mainPanel, layoutData);
    }

    private ContentPanel createDocumentList(final ListStore<GetProjectReports.ReportReference> store) {
        final ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setHeading(I18N.CONSTANTS.projectTabReports());

        // Toolbar
        final ToolBar toolBar = new ToolBar();
        toolBar.setAlignment(HorizontalAlignment.RIGHT);
        final IconImageBundle icons = GWT.create(IconImageBundle.class);
        toolBar.add(new Button(I18N.CONSTANTS.newText(), icons.add()));

        panel.setTopComponent(toolBar);

        // Report list
        final ColumnConfig editDate = new ColumnConfig("lastEditDate", I18N.CONSTANTS.date(), 200);
        editDate.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        final ColumnConfig editorName = new ColumnConfig("editorName", I18N.CONSTANTS.users(), 200);
        final ColumnConfig reportName = new ColumnConfig("name", I18N.CONSTANTS.name(), 200);
        final ColumnConfig phaseName = new ColumnConfig("phaseName", I18N.CONSTANTS.projectActivePhase(), 200);
        final ColumnModel reportColumnModel = new ColumnModel(Arrays.asList(editDate, editorName, reportName, phaseName));

        final Grid documentGrid = new Grid(store, reportColumnModel);
        documentGrid.setAutoExpandColumn("name");
        documentGrid.getView().setForceFit(true);

        documentGrid.addListener(Events.RowDoubleClick, new Listener<GridEvent>() {

            @Override
            public void handleEvent(GridEvent e) {
                final ProjectState state = new ProjectState(currentState.getProjectId());
                state.setCurrentSection(currentState.getCurrentSection());
                state.setArgument(store.getAt(e.getRowIndex()).getId().toString());

                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, state));
            }
        });

        panel.add(documentGrid);

        return panel;
    }

    private LayoutContainer createMainPanel() {
        final LayoutContainer panel = new LayoutContainer(new BorderLayout());
        return panel;
    }

    public void setCurrentState(ProjectState currentState) {
        this.currentState = currentState;
    }

    private void displaySection(final ProjectReportSectionDTO section, final FoldPanel parent, final StringBuilder prefix, int level) {
        final FoldPanel sectionPanel = new FoldPanel();
        sectionPanel.setHeading(prefix.toString() + ' ' + section.getName());
        sectionPanel.addStyleName("project-report-level-" + level);

        int index = 1;
        int prefixLength = prefix.length();
        for (final Serializable object : section.getChildren()) {
            if (object.getClass() == ProjectReportSectionDTO.class) {
                prefix.append(index).append('.');

                displaySection((ProjectReportSectionDTO) object, sectionPanel, prefix, level + 1);
                index++;

                prefix.setLength(prefixLength);

            } else if (object.getClass() == RichTextElementDTO.class) {
                final RichTextArea textArea = new RichTextArea();
                textArea.setText(((RichTextElementDTO) object).getText());

                textArea.addFocusHandler(new FocusHandler() {

                    @Override
                    public void onFocus(FocusEvent event) {
                        currentFormatter = textArea.getFormatter();
                    }
                });

                sectionPanel.add(textArea);

            } else {
                Log.warn("Report : object type unknown (" + object.getClass() + ")");
            }
        }

        parent.add(sectionPanel);
    }

    public void setReport(ProjectReportDTO report) {
        mainPanel.removeAll();

        if (report == null) {
            return;
        }

        // Title bar
        final ContentPanel reportPanel = new ContentPanel(new FitLayout()); //new RowLayout(Orientation.VERTICAL));
        reportPanel.setScrollMode(Scroll.AUTOY);
        reportPanel.setHeading(report.getName());

        final ToolButton closeButton = new ToolButton("x-tool-close");
        closeButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                final ProjectState state = new ProjectState(currentState.getProjectId());
                state.setCurrentSection(currentState.getCurrentSection());
                state.setArgument("-1");

                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, state));
            }
        });
        reportPanel.getHeader().addTool(closeButton);

        // Report
        final FoldPanel root = new FoldPanel();
        root.addStyleName("project-report");

        final List<ProjectReportSectionDTO> sections = report.getSections();

        final StringBuilder prefix = new StringBuilder();

        for (int index = 0; index < sections.size(); index++) {
            final ProjectReportSectionDTO section = sections.get(index);

            prefix.append(index + 1).append('.');
            displaySection(section, root, prefix, 1);

            prefix.setLength(0);
        }

        reportPanel.add(root);

        // Toolbar
        final ToolBar toolBar = new ToolBar();

        // Save button
        final IconImageBundle icons = GWT.create(IconImageBundle.class);
        final Button saveButton = new Button(I18N.CONSTANTS.save(), icons.save());
        toolBar.add(saveButton);

        toolBar.add(new SeparatorToolItem());

        // Overview mode
        final Button foldButton = new Button("Mode plan");
        foldButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                root.expand(true);
                root.fold(true);
            }
        });
        // Expanded mode
        final Button expandButton = new Button("Mode complet");
        expandButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                root.expand(true);
            }
        });

        toolBar.add(foldButton);
        toolBar.add(expandButton);
        toolBar.add(new SeparatorToolItem());
        createRichTextToolbar(toolBar);

        reportPanel.setTopComponent(toolBar);

        // Display
        mainPanel.add(reportPanel, new BorderLayoutData(LayoutRegion.CENTER));
        mainPanel.layout();

    }

    private void createRichTextToolbar(final ToolBar toolbar) {
        final ToolbarImages images = GWT.create(ToolbarImages.class);

        final ListBox fontListBox = new ListBox();
        fontListBox.addItem("Font");
        fontListBox.addItem("Arial");
        fontListBox.addItem("Times New Roman");
        fontListBox.addItem("Courier New");
        fontListBox.addItem("Georgia");
        fontListBox.addItem("Trebuchet");
        fontListBox.addItem("Verdana");
        fontListBox.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                currentFormatter.setFontName(fontListBox.getValue(fontListBox.getSelectedIndex()));
            }
        });
        final LayoutContainer fontListBoxWrapper = new LayoutContainer(new FitLayout());
        fontListBoxWrapper.add(fontListBox);
        toolbar.add(fontListBoxWrapper);

        final Button boldButton = new Button();
        boldButton.setIcon(AbstractImagePrototype.create(images.textBold()));
        boldButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleBold();
            }
        });
        toolbar.add(boldButton);

        final Button italicButton = new Button();
        italicButton.setIcon(AbstractImagePrototype.create(images.textItalic()));
        italicButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleItalic();
            }
        });
        toolbar.add(italicButton);

        final Button underlineButton = new Button();
        underlineButton.setIcon(AbstractImagePrototype.create(images.textUnderline()));
        underlineButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleUnderline();
            }
        });
        toolbar.add(underlineButton);

        final Button strikeButton = new Button();
        strikeButton.setIcon(AbstractImagePrototype.create(images.textStrike()));
        strikeButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleStrikethrough();
            }
        });
        toolbar.add(strikeButton);

        final Button alignLeftButton = new Button();
        alignLeftButton.setIcon(AbstractImagePrototype.create(images.textAlignLeft()));
        alignLeftButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.LEFT);
            }
        });
        toolbar.add(alignLeftButton);

        final Button alignCenterButton = new Button();
        alignCenterButton.setIcon(AbstractImagePrototype.create(images.textAlignCenter()));
        alignCenterButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.CENTER);
            }
        });
        toolbar.add(alignCenterButton);

        final Button alignRightButton = new Button();
        alignRightButton.setIcon(AbstractImagePrototype.create(images.textAlignRight()));
        alignRightButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.RIGHT);
            }
        });
        toolbar.add(alignRightButton);

        final Button alignJustifyButton = new Button();
        alignJustifyButton.setIcon(AbstractImagePrototype.create(images.textAlignJustify()));
        alignJustifyButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.FULL);
            }
        });
        toolbar.add(alignJustifyButton);

        final Button listNumbersButton = new Button();
        listNumbersButton.setIcon(AbstractImagePrototype.create(images.textListNumbers()));
        listNumbersButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.insertOrderedList();
            }
        });
        toolbar.add(listNumbersButton);

        final Button listBulletsButton = new Button();
        listBulletsButton.setIcon(AbstractImagePrototype.create(images.textListBullets()));
        listBulletsButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.insertUnorderedList();
            }
        });
        toolbar.add(listBulletsButton);
    }
}
