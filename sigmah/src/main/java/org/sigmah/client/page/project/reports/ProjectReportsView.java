/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.reports;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.page.project.reports.images.ToolbarImages;
import org.sigmah.client.ui.FoldPanel;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProjectReportModels;
import org.sigmah.shared.command.GetProjectReportModels.ModelReference;
import org.sigmah.shared.command.GetProjectReports;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectReportModelListResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.report.KeyQuestionDTO;
import org.sigmah.shared.dto.report.ProjectReportContent;
import org.sigmah.shared.dto.report.ProjectReportDTO;
import org.sigmah.shared.dto.report.ProjectReportSectionDTO;
import org.sigmah.shared.dto.report.RichTextElementDTO;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * Displays the reports attached to a project.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ProjectReportsView extends LayoutContainer {

    private Authentication authentication;
    private EventBus eventBus;
    private Dispatcher dispatcher;

    private ProjectState currentState;
    private String phaseName;

    private ListStore<GetProjectReports.ReportReference> store;
    private LayoutContainer mainPanel;
    private RichTextArea.Formatter currentFormatter;

    private HashMap<Integer, RichTextArea> textAreas;
    private KeyQuestionState keyQuestionState;

    private Dialog createReportDialog;

    private Button attachButton;

    public ProjectReportsView(Authentication authentication, EventBus eventBus, Dispatcher dispatcher,
            ListStore<GetProjectReports.ReportReference> store) {
        
        this.authentication = authentication;
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;
        this.textAreas = new HashMap<Integer, RichTextArea>();

        this.store = store;

        this.keyQuestionState = new KeyQuestionState();

        final BorderLayout layout = new BorderLayout();
        layout.setContainerStyle("x-border-layout-ct main-background"); // Adds
                                                                        // a
                                                                        // dark
                                                                        // background
                                                                        // between
                                                                        // objects
                                                                        // managed
                                                                        // by
                                                                        // this
                                                                        // layout
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

    private Dialog getCreateReportDialog(final ListStore<GetProjectReports.ReportReference> store) {
        if (createReportDialog == null) {
            final Dialog dialog = new Dialog();
            dialog.setButtons(Dialog.OKCANCEL);
            dialog.setHeading(I18N.CONSTANTS.reportCreateReport());
            dialog.setModal(true);

            dialog.setResizable(false);
            dialog.setWidth("340px");

            dialog.setLayout(new FormLayout());

            // Report name
            final TextField<String> nameField = new TextField<String>();
            nameField.setFieldLabel(I18N.CONSTANTS.reportName());
            nameField.setAllowBlank(false);
            nameField.setName("name");
            dialog.add(nameField);

            // Model list
            final ListStore<GetProjectReportModels.ModelReference> modelBoxStore = new ListStore<GetProjectReportModels.ModelReference>();
            final ComboBox<GetProjectReportModels.ModelReference> modelBox = new ComboBox<GetProjectReportModels.ModelReference>();
            modelBox.setFieldLabel(I18N.CONSTANTS.reportModel());
            modelBox.setStore(modelBoxStore);
            modelBox.setAllowBlank(false);
            modelBox.setDisplayField("name");
            modelBox.setName("model");
            modelBox.setTriggerAction(TriggerAction.ALL);
            modelBox.setEmptyText(I18N.CONSTANTS.reportEmptyChoice());
            dialog.add(modelBox);

            // Cancel button
            dialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    dialog.hide();
                }
            });

            createReportDialog = dialog;
        }

        final TextField<String> textField = (TextField<String>) createReportDialog.getWidget(0);
        textField.reset();

        final ComboBox<GetProjectReportModels.ModelReference> modelBox = (ComboBox<ModelReference>) createReportDialog
                .getWidget(1);
        modelBox.reset();
        final ListStore<GetProjectReportModels.ModelReference> modelBoxStore = modelBox.getStore();
        modelBoxStore.removeAll();

        dispatcher.execute(new GetProjectReportModels(), null, new AsyncCallback<ProjectReportModelListResult>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(ProjectReportModelListResult result) {
                modelBoxStore.add(result.getData());
            }

        });

        // OK Button
        final Button okButton = createReportDialog.getButtonById(Dialog.OK);

        okButton.removeAllListeners();
        okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final HashMap<String, Serializable> properties = new HashMap<String, Serializable>();

                final String name = ((TextField<String>) createReportDialog.getWidget(0)).getValue();
                final String phaseName = ProjectReportsView.this.phaseName;
                final String editorName = authentication.getUserShortName();
                final Date lastEditDate = new Date();

                properties.put("name", name);
                properties.put("reportModelId", modelBox.getSelection().get(0).getId());
                properties.put("phaseName", phaseName);
                properties.put("projectId", Integer.valueOf(currentState.getProjectId()));

                final CreateEntity createEntity = new CreateEntity("ProjectReport", properties);
                dispatcher.execute(createEntity, null, new AsyncCallback<CreateResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateError());
                    }

                    @Override
                    public void onSuccess(CreateResult result) {
                        final GetProjectReports.ReportReference report = new GetProjectReports.ReportReference();
                        report.setId(result.getNewId());
                        report.setName(name);
                        report.setPhaseName(phaseName);
                        report.setEditorName(editorName);
                        report.setLastEditDate(lastEditDate);

                        store.add(report);

                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateSuccess());
                    }
                });

                createReportDialog.hide();
            }

        });

        return createReportDialog;
    }

    private ContentPanel createDocumentList(final ListStore<GetProjectReports.ReportReference> store) {
        final ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setHeading(I18N.CONSTANTS.projectTabReports());

        // Toolbar
        final ToolBar toolBar = new ToolBar();
        toolBar.setAlignment(HorizontalAlignment.RIGHT);
        final IconImageBundle icons = GWT.create(IconImageBundle.class);

        final Button newButton = new Button(I18N.CONSTANTS.reportCreateReport(), icons.add());
        newButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                getCreateReportDialog(store).show();
            }
        });

        attachButton = new Button(I18N.CONSTANTS.flexibleElementFilesListAddDocument(), icons.attach());

        toolBar.add(attachButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(newButton);

        panel.setTopComponent(toolBar);

        // Report list
        final ColumnConfig editDate = new ColumnConfig("lastEditDate", I18N.CONSTANTS.reportLastEditDate(), 200);
        editDate.setDateTimeFormat(DateTimeFormat.getShortDateFormat());
        final ColumnConfig editorName = new ColumnConfig("editorName", I18N.CONSTANTS.reportEditor(), 200);
        final ColumnConfig iconColumn = new ColumnConfig("icon", "", 20);
        final ColumnConfig reportName = new ColumnConfig("name", I18N.CONSTANTS.reportName(), 200);
        final ColumnConfig typeColumn = new ColumnConfig("flexibleElementLabel", I18N.CONSTANTS.reportType(), 200);
        final ColumnConfig phaseNameColumn = new ColumnConfig("phaseName", I18N.CONSTANTS.reportPhase(), 200);
        final ColumnModel reportColumnModel = new ColumnModel(Arrays.asList(editDate, editorName, iconColumn,
                reportName, typeColumn, phaseNameColumn));

        iconColumn.setRenderer(new GridCellRenderer<GetProjectReports.ReportReference>() {

            @Override
            public Object render(GetProjectReports.ReportReference model, String property, ColumnData config,
                    int rowIndex, int colIndex, ListStore<GetProjectReports.ReportReference> store,
                    Grid<GetProjectReports.ReportReference> grid) {

                if (model.isDocument()) {
                    return IconImageBundle.ICONS.attach().createImage();
                } else {
                    return IconImageBundle.ICONS.report().createImage();
                }
            }
        });

        reportName.setRenderer(new GridCellRenderer<GetProjectReports.ReportReference>() {
            @Override
            public Object render(final GetProjectReports.ReportReference model, String property, ColumnData config,
                    int rowIndex, int colIndex, ListStore store, Grid grid) {

                if (model.isDocument()) {

                    // Uses a "hidden form" to manages the downloads to be able
                    // to catch server errors.
                    final FormPanel downloadFormPanel = new FormPanel();
                    downloadFormPanel.setBodyBorder(false);
                    downloadFormPanel.setHeaderVisible(false);
                    downloadFormPanel.setPadding(0);
                    downloadFormPanel.setEncoding(Encoding.URLENCODED);
                    downloadFormPanel.setMethod(Method.GET);
                    downloadFormPanel.setAction(GWT.getModuleBaseURL() + "download");

                    final com.google.gwt.user.client.ui.Label downloadButton = new com.google.gwt.user.client.ui.Label(
                            (String) model.get(property));
                    downloadButton.addStyleName("flexibility-action");

                    // File's id.
                    final HiddenField<String> fileIdHidden = new HiddenField<String>();
                    fileIdHidden.setName(FileUploadUtils.DOCUMENT_ID);
                    fileIdHidden.setValue(String.valueOf(model.getId()));

                    downloadFormPanel.add(downloadButton);
                    downloadFormPanel.add(fileIdHidden);

                    // Submit complete handler to catch server errors.
                    downloadFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {

                        @Override
                        public void handleEvent(FormEvent be) {

                            if (!"".equals(be.getResultHtml())) {
                                MessageBox.info(I18N.CONSTANTS.flexibleElementFilesListDownloadError(),
                                        I18N.CONSTANTS.flexibleElementFilesListDownloadErrorDetails(), null);
                            }
                        }
                    });

                    // Buttons listeners.
                    downloadButton.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent e) {

                            // Submits the form.
                            downloadFormPanel.submit();
                        }
                    });

                    return downloadFormPanel;

                } else {

                    final Anchor link = new Anchor((String) model.get(property));
                    link.addStyleName("flexibility-action");

                    link.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            // Opening a report

                            final ProjectState state = new ProjectState(currentState.getProjectId());
                            state.setCurrentSection(currentState.getCurrentSection());
                            state.setArgument(model.getId().toString());

                            eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, state));
                        }
                    });

                    return link;
                }
            }
        });

        final Grid<GetProjectReports.ReportReference> documentGrid = new Grid<GetProjectReports.ReportReference>(store,
                reportColumnModel);
        documentGrid.setAutoExpandColumn("name");
        documentGrid.getView().setForceFit(true);

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

    private void displaySection(final ProjectReportSectionDTO section, final FoldPanel parent,
            final StringBuilder prefix, int level) {
        final FoldPanel sectionPanel = new FoldPanel();
        sectionPanel.setHeading(prefix.toString() + ' ' + section.getName());
        sectionPanel.addStyleName("project-report-level-" + level);

        int index = 1;
        int prefixLength = prefix.length();
        for (final ProjectReportContent object : section.getChildren()) {
            if (object.getClass() == ProjectReportSectionDTO.class) {
                prefix.append(index).append('.');

                displaySection((ProjectReportSectionDTO) object, sectionPanel, prefix, level + 1);
                index++;

                prefix.setLength(prefixLength);

            } else if (object.getClass() == RichTextElementDTO.class) {
                final RichTextArea textArea = new RichTextArea();
                textArea.setHTML(((RichTextElementDTO) object).getText());

                textArea.addFocusHandler(new FocusHandler() {

                    @Override
                    public void onFocus(FocusEvent event) {
                        currentFormatter = textArea.getFormatter();
                    }
                });

                sectionPanel.add(textArea);
                textAreas.put(((RichTextElementDTO) object).getId(), textArea);

            } else if (object.getClass() == KeyQuestionDTO.class) {
                final KeyQuestionDTO keyQuestion = (KeyQuestionDTO) object;
                keyQuestionState.increaseCount();

                keyQuestion.setNumber(keyQuestionState.getCount());

                // Rich text field
                final RichTextArea textArea = new RichTextArea();
                final RichTextElementDTO richTextElementDTO = keyQuestion.getRichTextElementDTO();
                if (richTextElementDTO != null) {
                    textArea.setHTML(richTextElementDTO.getText());
                    textAreas.put(richTextElementDTO.getId(), textArea);

                } else {
                    Log.error("No text area is attached to the key question #" + keyQuestion.getId());
                }

                // Compas icon
                final ToolbarImages images = GWT.create(ToolbarImages.class);

                final ImageResource icon;
                if ("".equals(textArea.getText()))
                    icon = images.compasRed();
                else {
                    icon = images.compasGreen();
                    keyQuestionState.increaseValids();
                }

                final int toolButtonIndex = sectionPanel.getToolButtonCount();

                sectionPanel.addToolButton(icon, new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        KeyQuestionDialog.getDialog(keyQuestion, textArea, sectionPanel, toolButtonIndex,
                                keyQuestionState).show();
                    }
                });

            } else {
                Log.warn("Report : object type unknown (" + object.getClass() + ")");
            }
        }

        parent.add(sectionPanel);
    }

    public void setReport(final ProjectReportDTO report) {
        mainPanel.removeAll();

        if (report == null)
            return;

        // Preparing the view for the new report
        textAreas.clear();
        keyQuestionState.clear();

        // Title bar
        final ContentPanel reportPanel = new ContentPanel(new FitLayout()); // new
                                                                            // RowLayout(Orientation.VERTICAL));
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
        saveButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                final HashMap<String, String> changes = new HashMap<String, String>();

                changes.put("currentPhase", phaseName);

                for (final Map.Entry<Integer, RichTextArea> entry : textAreas.entrySet())
                    changes.put(entry.getKey().toString(), entry.getValue().getHTML());

                final UpdateEntity updateEntity = new UpdateEntity("ProjectReport", report.getId(),
                        (Map<String, Object>) (Map<String, ?>) changes);
                dispatcher.execute(updateEntity, null, new AsyncCallback<VoidResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportSaveError());
                    }

                    @Override
                    public void onSuccess(VoidResult result) {
                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportSaveSuccess());

                        boolean found = false;
                        for (int index = 0; !found && index < store.getCount(); index++) {
                            final GetProjectReports.ReportReference reference = store.getAt(index);

                            if (reference.getId().equals(report.getId())) {
                                store.remove(reference);

                                reference.setEditorName(authentication.getUserShortName());
                                reference.setPhaseName(phaseName);
                                reference.setLastEditDate(new Date());

                                store.add(reference);

                                found = true;
                            }
                        }
                    }

                });
            }
        });

        toolBar.add(saveButton);
        toolBar.add(new SeparatorToolItem());

        // Key question info
        final Label keyQuestionLabel = keyQuestionState.getLabel();
        toolBar.add(keyQuestionLabel);
        toolBar.add(new SeparatorToolItem());

        // Overview mode
        final Button foldButton = new Button(I18N.CONSTANTS.reportOverviewMode());
        foldButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                root.expand(true);
                root.fold(true);
            }
        });
        // Expanded mode
        final Button expandButton = new Button(I18N.CONSTANTS.reportFullMode());
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

        // Fonts
        final ListBox fontListBox = new ListBox();
        fontListBox.addItem(I18N.CONSTANTS.font());
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

        // Bold
        final Button boldButton = new Button();
        boldButton.setIcon(AbstractImagePrototype.create(images.textBold()));
        boldButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleBold();
            }
        });
        toolbar.add(boldButton);

        // Italic
        final Button italicButton = new Button();
        italicButton.setIcon(AbstractImagePrototype.create(images.textItalic()));
        italicButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleItalic();
            }
        });
        toolbar.add(italicButton);

        // Underline
        final Button underlineButton = new Button();
        underlineButton.setIcon(AbstractImagePrototype.create(images.textUnderline()));
        underlineButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleUnderline();
            }
        });
        toolbar.add(underlineButton);

        // Strike
        final Button strikeButton = new Button();
        strikeButton.setIcon(AbstractImagePrototype.create(images.textStrike()));
        strikeButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.toggleStrikethrough();
            }
        });
        toolbar.add(strikeButton);

        // Align left
        final Button alignLeftButton = new Button();
        alignLeftButton.setIcon(AbstractImagePrototype.create(images.textAlignLeft()));
        alignLeftButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.LEFT);
            }
        });
        toolbar.add(alignLeftButton);

        // Align center
        final Button alignCenterButton = new Button();
        alignCenterButton.setIcon(AbstractImagePrototype.create(images.textAlignCenter()));
        alignCenterButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.CENTER);
            }
        });
        toolbar.add(alignCenterButton);

        // Align right
        final Button alignRightButton = new Button();
        alignRightButton.setIcon(AbstractImagePrototype.create(images.textAlignRight()));
        alignRightButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.RIGHT);
            }
        });
        toolbar.add(alignRightButton);

        // Justify
        final Button alignJustifyButton = new Button();
        alignJustifyButton.setIcon(AbstractImagePrototype.create(images.textAlignJustify()));
        alignJustifyButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.setJustification(RichTextArea.Justification.FULL);
            }
        });
        toolbar.add(alignJustifyButton);

        // List with numbers
        final Button listNumbersButton = new Button();
        listNumbersButton.setIcon(AbstractImagePrototype.create(images.textListNumbers()));
        listNumbersButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.insertOrderedList();
            }
        });
        toolbar.add(listNumbersButton);

        // List with bullets
        final Button listBulletsButton = new Button();
        listBulletsButton.setIcon(AbstractImagePrototype.create(images.textListBullets()));
        listBulletsButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                currentFormatter.insertUnorderedList();
            }
        });
        toolbar.add(listBulletsButton);

        // Images
        final Button imageAddButton = new Button();
        imageAddButton.setIcon(AbstractImagePrototype.create(images.imageAdd()));
        imageAddButton.addListener(Events.Select, new Listener<BaseEvent>() {
            private Dialog imageAddDialog;
            private TextField<String> imageURLField;

            @Override
            public void handleEvent(BaseEvent be) {
                if (imageAddDialog == null) {
                    imageAddDialog = new Dialog();

                    imageAddDialog.setButtons(Dialog.OKCANCEL);
                    imageAddDialog.setHeading(I18N.CONSTANTS.reportAddImageDialogTitle());
                    imageAddDialog.setModal(true);

                    imageAddDialog.setResizable(false);
                    imageAddDialog.setWidth("340px");

                    imageAddDialog.setLayout(new FormLayout());

                    // Report name
                    imageURLField = new TextField<String>();
                    imageURLField.setFieldLabel(I18N.CONSTANTS.reportImageURL());
                    imageURLField.setAllowBlank(false);
                    imageURLField.setName("url");
                    imageAddDialog.add(imageURLField);

                    // OK button
                    imageAddDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {
                        @Override
                        public void componentSelected(ButtonEvent ce) {
                            currentFormatter.insertImage(imageURLField.getValue());
                            imageAddDialog.hide();
                        }
                    });

                    // Cancel button
                    imageAddDialog.getButtonById(Dialog.CANCEL).addSelectionListener(
                            new SelectionListener<ButtonEvent>() {
                                @Override
                                public void componentSelected(ButtonEvent ce) {
                                    imageAddDialog.hide();
                                }
                            });
                }

                imageURLField.setValue(null);
                imageAddDialog.show();
            }
        });
        toolbar.add(imageAddButton);
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public Button getAttachButton() {
        return attachButton;
    }
}
