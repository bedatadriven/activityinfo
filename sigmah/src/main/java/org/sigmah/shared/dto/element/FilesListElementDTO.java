/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.ui.FlexibleGrid;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.value.FileDTO;
import org.sigmah.shared.dto.value.FileUploadUtils;
import org.sigmah.shared.dto.value.FileVersionDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class FilesListElementDTO extends FlexibleElementDTO {

    private static final long serialVersionUID = 8520711106031085130L;

    /**
     * Current value result updated after each upload to keep the consistency of
     * the widget.
     */
    private ValueResult currentValueResult;

    /**
     * Files list model data.
     */
    private transient ListStore<FileDTO> store;

    @Override
    public String getEntityName() {
        // Gets the entity name mapped by the current DTO starting from the
        // "server.domain" package name.
        return "element.FilesListElement";
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {

        if (result == null || !result.isValueDefined()) {
            return false;
        }

        return !result.getValuesObject().isEmpty();
    }

    /**
     * Updates the grid store for the current value.
     */
    private void updateStore() {

        if (currentValueResult != null && currentValueResult.isValueDefined()) {
            store.removeAll();
            for (Serializable s : currentValueResult.getValuesObject()) {
                store.add((FileDTO) s);
            }
        }
    }

    @Override
    public void init() {
        super.init();
        store = new ListStore<FileDTO>();
    }

    @Override
    public Component getComponent(ValueResult valueResult) {

        currentValueResult = valueResult;

        // Creates actions menu to manage the files list.
        final Button downloadButton = new Button(I18N.CONSTANTS.flexibleElementFilesListDownload());
        downloadButton.setEnabled(false);

        // Uses a "hidden form" to manages the downloads to be able to catch
        // server errors.
        final FormPanel downloadFormPanel = new FormPanel();
        downloadFormPanel.setBodyBorder(false);
        downloadFormPanel.setHeaderVisible(false);
        downloadFormPanel.setPadding(0);
        downloadFormPanel.setEncoding(Encoding.URLENCODED);
        downloadFormPanel.setMethod(Method.GET);
        downloadFormPanel.setAction(GWT.getModuleBaseURL() + "download");

        final HiddenField<String> fileIdHidden = new HiddenField<String>();
        fileIdHidden.setName(FileUploadUtils.DOCUMENT_ID);

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

        final Button detailsButton = new Button(I18N.CONSTANTS.flexibleElementFilesListDetails());
        detailsButton.setEnabled(false);

        final Button addVersionButton = new Button(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());
        addVersionButton.setEnabled(false);

        final Button deleteButton = new Button(I18N.CONSTANTS.remove());
        deleteButton.setEnabled(false);

        final ToolBar actionsToolBar = new ToolBar();
        actionsToolBar.add(downloadFormPanel);
        actionsToolBar.add(new SeparatorToolItem());
        actionsToolBar.add(detailsButton);
        actionsToolBar.add(new SeparatorToolItem());
        actionsToolBar.add(addVersionButton);
        actionsToolBar.add(new SeparatorToolItem());
        actionsToolBar.add(deleteButton);

        // Creates the upload field and upload button.
        final FileUploadField uploadField = new FileUploadField();
        uploadField.setFieldLabel(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());
        uploadField.setName(FileUploadUtils.DOCUMENT_CONTENT);

        final Button uploadButton = new Button(I18N.CONSTANTS.flexibleElementFilesListUpload());
        uploadButton.setEnabled(false);

        final ContentPanel uploadPanel = new ContentPanel();
        uploadPanel.setBodyBorder(false);
        uploadPanel.setHeaderVisible(false);
        uploadPanel.setLayout(new HBoxLayout());

        final Label addVersionLabel = new Label(I18N.CONSTANTS.flexibleElementFilesListUploadFile());
        addVersionLabel.addStyleName("flexibility-element-label");
        uploadPanel.add(addVersionLabel, new HBoxLayoutData(new Margins(4, 5, 0, 0)));
        final HBoxLayoutData flex = new HBoxLayoutData(new Margins(0, 5, 0, 0));
        flex.setFlex(1);
        uploadPanel.add(uploadField, flex);
        uploadPanel.add(uploadButton);

        final FormPanel uploadFormPanel = new FormPanel();
        uploadFormPanel.setBodyBorder(false);
        uploadFormPanel.setHeaderVisible(false);
        uploadFormPanel.setPadding(0);
        uploadFormPanel.setEncoding(Encoding.MULTIPART);
        uploadFormPanel.setMethod(Method.POST);
        uploadFormPanel.setAction(GWT.getModuleBaseURL() + "upload");

        final HiddenField<String> elementIdHidden = new HiddenField<String>();
        elementIdHidden.setName(FileUploadUtils.DOCUMENT_FLEXIBLE_ELEMENT);

        final HiddenField<String> projectIdHidden = new HiddenField<String>();
        projectIdHidden.setName(FileUploadUtils.DOCUMENT_PROJECT);

        final HiddenField<String> nameHidden = new HiddenField<String>();
        nameHidden.setName(FileUploadUtils.DOCUMENT_NAME);

        final HiddenField<String> authorHidden = new HiddenField<String>();
        authorHidden.setName(FileUploadUtils.DOCUMENT_AUTHOR);

        final HiddenField<String> emptyHidden = new HiddenField<String>();
        emptyHidden.setName(FileUploadUtils.CHECK_EMPTY);

        uploadFormPanel.add(uploadPanel);
        uploadFormPanel.add(nameHidden);
        uploadFormPanel.add(authorHidden);
        uploadFormPanel.add(elementIdHidden);
        uploadFormPanel.add(projectIdHidden);
        uploadFormPanel.add(emptyHidden);

        // Manages upload button activations.
        uploadField.addListener(Events.OnChange, new Listener<ComponentEvent>() {
            @Override
            public void handleEvent(ComponentEvent be) {
                uploadButton.setEnabled(uploadField.getValue() != null && !uploadField.getValue().trim().equals(""));
            }
        });

        uploadButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {

            @Override
            public void handleEvent(ButtonEvent be) {

                // Set hidden fields values.
                elementIdHidden.setValue(String.valueOf(getId()));
                projectIdHidden.setValue(String.valueOf(currentProjectDTO.getId()));
                nameHidden.setValue(uploadField.getValue());
                authorHidden.setValue(String.valueOf(authentication.getUserId()));
                emptyHidden.setValue("true");

                // Debug form hidden values.
                if (Log.isDebugEnabled()) {

                    final StringBuilder sb = new StringBuilder();
                    sb.append("Upload a new file with parameters: ");
                    sb.append("name=");
                    sb.append(nameHidden.getValue());
                    sb.append(" ; author id=");
                    sb.append(authorHidden.getValue());
                    // sb.append(" ; file list id=");
                    // sb.append(filesListHidden.getValue());
                    sb.append(" ; project id=");
                    sb.append(projectIdHidden.getValue());
                    sb.append(" ; element id=");
                    sb.append(elementIdHidden.getValue());
                    sb.append(" ; allow empty=");
                    sb.append(emptyHidden.getValue());

                    Log.debug(sb.toString());
                }

                // Freezes upload fields.
                uploadField.setEnabled(false);
                uploadButton.setEnabled(false);

                // Submits the form.
                uploadFormPanel.submit();
            }
        });

        uploadFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {

                // Updates the widget for the new value.
                updateComponentAfterUpload(be);

                // Reset upload fields.
                uploadField.setEnabled(true);
                uploadField.reset();

                handlerManager.fireEvent(new RequiredValueEvent(true));
            }
        });

        // Creates the top panel of the grid.
        final ContentPanel topPanel = new ContentPanel();
        topPanel.setHeaderVisible(false);
        topPanel.setLayout(new FlowLayout());

        topPanel.add(uploadFormPanel, new FlowData(new Margins(3, 2, 3, 2)));
        topPanel.add(actionsToolBar);

        updateStore();

        // Grid plugins.
        final CheckBoxSelectionModel<FileDTO> selectionModel = new CheckBoxSelectionModel<FileDTO>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        // Creates the grid which contains the files list.
        final FlexibleGrid<FileDTO> filesGrid = new FlexibleGrid<FileDTO>(store, selectionModel,
                getColumnModel(selectionModel));
        filesGrid.setAutoExpandColumn("name");
        filesGrid.setVisibleElementsCount(5);

        store.setStoreSorter(new StoreSorter<FileDTO>() {
            @Override
            public int compare(Store<FileDTO> store, FileDTO m1, FileDTO m2, String property) {

                if ("date".equals(property)) {

                    final FileVersionDTO last1 = m1.getLastVersion();
                    final FileVersionDTO last2 = m2.getLastVersion();

                    return last1.getAddedDate().compareTo(last2.getAddedDate());
                } else if ("author".equals(property)) {

                    final FileVersionDTO last1 = m1.getLastVersion();
                    final FileVersionDTO last2 = m2.getLastVersion();

                    final String authorM1 = last1.getAuthorFirstName() != null ? last1.getAuthorFirstName() + " "
                            + last1.getAuthorName() : last1.getAuthorName();
                    final String authorM2 = last2.getAuthorFirstName() != null ? last2.getAuthorFirstName() + " "
                            + last2.getAuthorName() : last2.getAuthorName();

                    return authorM1.compareTo(authorM2);
                } else if ("version".equals(property)) {

                    final FileVersionDTO last1 = m1.getLastVersion();
                    final FileVersionDTO last2 = m2.getLastVersion();

                    return new Integer(last1.getVersionNumber()).compareTo(last2.getVersionNumber());
                } else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });

        // Creates the main panel.
        final ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(true);
        panel.setBorders(true);
        panel.setHeading(getLabel());

        panel.setTopComponent(topPanel);
        panel.add(filesGrid);

        // Manages action buttons activations.
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<FileDTO>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<FileDTO> se) {
                final List<FileDTO> selection = se.getSelection();
                final boolean enabledState = selection != null && !selection.isEmpty();
                downloadButton.setEnabled(enabledState);
                detailsButton.setEnabled(enabledState);
                addVersionButton.setEnabled(enabledState);
                deleteButton.setEnabled(enabledState);
            }
        });

        // Buttons listeners.
        downloadButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {

                // Sets the form hidden fields values.
                fileIdHidden.setValue(String.valueOf(selectionModel.getSelectedItem().getId()));

                // Submits the form.
                downloadFormPanel.submit();
            }
        });

        // Builds the file's details window to show the file's versions.
        final FileDetailsWindow versionsWindow = new FileDetailsWindow(dispatcher);
        versionsWindow.addListener(new FileDetailsWindow.FileDetailsWindowListener() {
            @Override
            public void versionDeleted(FileVersionDTO version) {
                updateComponent();
            }
        });

        detailsButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {

                // Loads and shows the details window.
                final FileDTO file = selectionModel.getSelectedItem();
                versionsWindow.show(file);
            }
        });

        addVersionButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {

            private final VersionUploadWindow window = new VersionUploadWindow();

            @Override
            public void handleEvent(ButtonEvent be) {
                final FileDTO file = selectionModel.getSelectedItem();
                window.show(file);
            }
        });

        deleteButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {

                final FileDTO file = selectionModel.getSelectedItem();

                // Asks the client to confirm the file removal.
                MessageBox.confirm(I18N.CONSTANTS.flexibleElementFilesListDelete(),
                        I18N.MESSAGES.flexibleElementFilesListConfirmDelete(file.getName()),
                        new Listener<MessageBoxEvent>() {
                            @Override
                            public void handleEvent(MessageBoxEvent ce) {

                                if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {

                                    // Deletes it.
                                    dispatcher.execute(new Delete(file),
                                            new MaskingAsyncMonitor(panel, I18N.CONSTANTS.loading()),
                                            new AsyncCallback<VoidResult>() {

                                                public void onFailure(Throwable caught) {
                                                    MessageBox.alert(
                                                            I18N.CONSTANTS.flexibleElementFilesListDeleteError(),
                                                            I18N.CONSTANTS.flexibleElementFilesListDeleteErrorDetails(),
                                                            null);
                                                }

                                                public void onSuccess(VoidResult result) {
                                                    store.remove(file);
                                                    if (store.getCount() == 0) {
                                                        handlerManager.fireEvent(new RequiredValueEvent(false));
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });

        return panel;
    }

    /**
     * Update files list after an upload.
     * 
     * @param be
     *            Form event after the upload.
     */
    private void updateComponentAfterUpload(FormEvent be) {

        final String code = FileUploadUtils.parseUploadResult(be.getResultHtml());

        if (FileUploadUtils.isError(code)) {

            // If an error occurred, informs the user.
            final StringBuilder sb = new StringBuilder();
            sb.append(I18N.CONSTANTS.flexibleElementFilesListUploadErrorDetails());

            if (FileUploadUtils.EMPTY_DOC_ERROR_CODE.equals(code)) {
                sb.append("\n");
                sb.append(I18N.CONSTANTS.flexibleElementFilesListUploadErrorEmpty());
            } else if (FileUploadUtils.TOO_BIG_DOC_ERROR_CODE.equals(code)) {
                sb.append("\n");
                sb.append(I18N.CONSTANTS.flexibleElementFilesListUploadErrorTooBig());
            }

            MessageBox.info(I18N.CONSTANTS.flexibleElementFilesListUploadError(), sb.toString(), null);
        } else {
            updateComponent();
        }
    }

    /**
     * Refreshes files list
     */
    private void updateComponent() {
        final GetValue command = new GetValue(currentProjectDTO.getId(), getId(), getEntityName());

        // Server call to obtain elements value
        dispatcher.execute(command, null, new AsyncCallback<ValueResult>() {

            @Override
            public void onFailure(Throwable throwable) {
                // The widget cannot be refreshed for the new value state.
            }

            @Override
            public void onSuccess(ValueResult valueResult) {

                currentValueResult = valueResult;
                updateStore();
            }
        });
    }

    /**
     * Defines the column model for the files list grid.
     * 
     * @param selectionModel
     *            The grid selection model.
     * @return The column model.
     */
    private ColumnConfig[] getColumnModel(CheckBoxSelectionModel<FileDTO> selectionModel) {

        // File's add date.
        final ColumnConfig dateColumn = new ColumnConfig();
        dateColumn.setId("date");
        dateColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListDate());
        dateColumn.setWidth(60);
        dateColumn.setRenderer(new GridCellRenderer<FileDTO>() {

            final DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy HH:mm");

            @Override
            public Object render(FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {
                final FileVersionDTO last = model.getLastVersion();
                return format.format(last.getAddedDate());
            }
        });

        // File's name.
        final ColumnConfig nameColumn = new ColumnConfig();
        nameColumn.setId("name");
        nameColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListName());
        TextField<String> text = new TextField<String>();
        text.setAllowBlank(false);
        nameColumn.setWidth(100);

        // File's author.
        final ColumnConfig authorColumn = new ColumnConfig();
        authorColumn.setId("author");
        authorColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListAuthor());
        authorColumn.setWidth(100);
        authorColumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {
                final FileVersionDTO last = model.getLastVersion();
                return last.getAuthorFirstName() != null ? last.getAuthorFirstName() + " " + last.getAuthorName()
                        : last.getAuthorName();
            }
        });

        // File's last version number.
        final ColumnConfig versioncolumn = new ColumnConfig();
        versioncolumn.setId("version");
        versioncolumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListVersion());
        versioncolumn.setWidth(20);
        versioncolumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {
                final FileVersionDTO last = model.getLastVersion();
                return last.getVersionNumber();
            }
        });

        return new ColumnConfig[] { selectionModel.getColumn(), dateColumn, nameColumn, authorColumn, versioncolumn };
    }

    /**
     * Builds and shows a window to upload a new version.
     * 
     * @author tmi
     */
    private final class VersionUploadWindow {

        /**
         * GXT window.
         */
        private final Window window;

        /**
         * The current displayed file.
         */
        private FileDTO file;

        /**
         * The next version number to upload.
         */
        private int nextVersionNumber;

        /**
         * The upload form.
         */
        private final FormPanel uploadFormPanel;

        /**
         * The upload field.
         */
        private final FileUploadField uploadField;

        /**
         * The label containing the next version number.
         */
        private final Label numberLabel;

        /**
         * Current logged in user.
         */
        private final HiddenField<String> authorHidden;

        /**
         * Current file id.
         */
        private final HiddenField<String> idHidden;

        /**
         * Next file version number.
         */
        private final HiddenField<String> versionHidden;

        /**
         * If empty version are allowed.
         */
        private final HiddenField<String> emptyHidden;

        /**
         * The upload button.
         */
        private final Button uploadButton;

        /**
         * The comments area.
         */
        private final TextArea commentsArea;

        public VersionUploadWindow() {

            // Creates the upload field and upload button.
            uploadField = new FileUploadField();
            uploadField.setFieldLabel(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());
            uploadField.setName(FileUploadUtils.DOCUMENT_CONTENT);

            uploadButton = new Button(I18N.CONSTANTS.flexibleElementFilesListUpload());
            uploadButton.setEnabled(false);

            final ContentPanel uploadPanel = new ContentPanel();
            uploadPanel.setBodyBorder(false);
            uploadPanel.setHeaderVisible(false);
            uploadPanel.setLayout(new HBoxLayout());

            final Label addVersionLabel = new Label(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());
            addVersionLabel.addStyleName("flexibility-element-label");
            uploadPanel.add(addVersionLabel, new HBoxLayoutData(new Margins(4, 5, 0, 0)));
            final HBoxLayoutData flex = new HBoxLayoutData(new Margins(0, 5, 0, 0));
            flex.setFlex(1);

            uploadPanel.add(uploadField, flex);
            uploadPanel.add(uploadButton);

            final ContentPanel commentsPanel = new ContentPanel();
            commentsPanel.setBodyBorder(false);
            commentsPanel.setHeaderVisible(false);
            commentsPanel.setLayout(new VBoxLayout());
            commentsPanel.setHeight(75);

            final Label commentsLabel = new Label(I18N.CONSTANTS.flexibleElementFilesListComments());
            commentsLabel.addStyleName("flexibility-element-label");
            commentsLabel.setLabelFor("VersionUploadWindowCommentsArea-input");

            commentsArea = new TextArea();
            commentsArea.setId("VersionUploadWindowCommentsArea");
            commentsArea.setName(FileUploadUtils.DOCUMENT_COMMENTS);
            commentsArea.setWidth("100%");
            commentsArea.setHeight(55);

            commentsPanel.setTopComponent(commentsLabel);
            commentsPanel.add(commentsArea, new VBoxLayoutData(new Margins(3, 0, 0, 0)));

            uploadFormPanel = new FormPanel();
            uploadFormPanel.setBodyBorder(false);
            uploadFormPanel.setHeaderVisible(false);
            uploadFormPanel.setPadding(0);
            uploadFormPanel.setEncoding(Encoding.MULTIPART);
            uploadFormPanel.setMethod(Method.POST);
            uploadFormPanel.setAction(GWT.getModuleBaseURL() + "upload");

            idHidden = new HiddenField<String>();
            idHidden.setName(FileUploadUtils.DOCUMENT_ID);

            authorHidden = new HiddenField<String>();
            authorHidden.setName(FileUploadUtils.DOCUMENT_AUTHOR);

            versionHidden = new HiddenField<String>();
            versionHidden.setName(FileUploadUtils.DOCUMENT_VERSION);

            emptyHidden = new HiddenField<String>();
            emptyHidden.setName(FileUploadUtils.CHECK_EMPTY);

            uploadFormPanel.add(uploadPanel);
            uploadFormPanel.add(commentsPanel);
            uploadFormPanel.add(authorHidden);
            uploadFormPanel.add(idHidden);
            uploadFormPanel.add(versionHidden);
            uploadFormPanel.add(emptyHidden);

            // Manages upload button activations.
            uploadField.addListener(Events.OnChange, new Listener<ComponentEvent>() {
                @Override
                public void handleEvent(ComponentEvent be) {
                    uploadButton.setEnabled(uploadField.getValue() != null && !uploadField.getValue().trim().equals(""));
                }
            });

            uploadButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {

                @Override
                public void handleEvent(ButtonEvent be) {

                    // Set hidden fields values.
                    idHidden.setValue(String.valueOf(file.getId()));
                    authorHidden.setValue(String.valueOf(authentication.getUserId()));
                    versionHidden.setValue(String.valueOf(nextVersionNumber));
                    emptyHidden.setValue("true");

                    // Debug form hidden values.
                    if (Log.isDebugEnabled()) {

                        final StringBuilder sb = new StringBuilder();
                        sb.append("Upload a new version with parameters: ");
                        sb.append("version number=");
                        sb.append(versionHidden.getValue());
                        sb.append(" ; file id=");
                        sb.append(idHidden.getValue());
                        sb.append(" ; author id=");
                        sb.append(authorHidden.getValue());
                        sb.append(" ; allow empty=");
                        sb.append(emptyHidden.getValue());

                        Log.debug(sb.toString());
                    }

                    // Freezes upload fields.
                    uploadField.setEnabled(false);
                    uploadButton.setEnabled(false);

                    // Submits the form.
                    uploadFormPanel.submit();
                }
            });

            uploadFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {

                @Override
                public void handleEvent(FormEvent be) {

                    // Updates the widget for the new value.
                    updateComponentAfterUpload(be);

                    // Reset upload fields.
                    uploadField.setEnabled(true);
                    uploadField.reset();

                    window.hide();
                }
            });

            // Creates the properties panel.
            final ContentPanel propertiesPanel = new ContentPanel();
            propertiesPanel.setHeading(I18N.CONSTANTS.flexibleElementFilesListProperties());
            propertiesPanel.setLayout(new FlowLayout());

            final com.google.gwt.user.client.ui.Grid grid = new com.google.gwt.user.client.ui.Grid(1, 2);
            grid.getColumnFormatter().setWidth(0, "128px");
            grid.setWidth("100%");

            // Displays file's name.
            final Label nameLabel = new Label(I18N.CONSTANTS.flexibleElementFilesListNextVersion() + ": ");
            nameLabel.addStyleName("flexibility-element-field-label");
            grid.setWidget(0, 0, nameLabel);
            numberLabel = new Label();
            grid.setWidget(0, 1, numberLabel);
            propertiesPanel.add(grid);

            // Creates the main panel of the window.
            final ContentPanel mainPanel = new ContentPanel();
            mainPanel.setHeaderVisible(false);
            mainPanel.setLayout(new FlowLayout());

            mainPanel.add(propertiesPanel);
            mainPanel.add(uploadFormPanel, new FlowData(new Margins(3, 5, 3, 5)));

            // Builds window.
            window = new Window();
            window.setSize(500, 180);
            window.setPlain(true);
            window.setModal(true);
            window.setBlinkModal(true);
            window.setLayout(new FitLayout());

            window.add(mainPanel);
        }

        /**
         * Shows the windows for the given file.
         * 
         * @param file
         *            The file.
         */
        public void show(FileDTO file) {

            if (file == null) {
                return;
            }

            this.file = file;

            // Configures the window parameters to be consistent with the new
            // displayed file.
            nextVersionNumber = this.file.getLastVersion().getVersionNumber() + 1;

            numberLabel.setText("#" + String.valueOf(nextVersionNumber));
            commentsArea.reset();
            window.setHeading(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());

            window.show();
        }
    }

    /**
     * Builds and shows a window with the file's details.
     * 
     * @author tmi
     * 
     */
    private static final class FileDetailsWindow {

        private interface FileDetailsWindowListener {
            public void versionDeleted(FileVersionDTO version);
        }

        /**
         * GXT window.
         */
        private final Window window;

        /**
         * The current displayed file.
         */
        private FileDTO file;

        /**
         * Versions store.
         */
        private final ListStore<FileVersionDTO> store;

        /**
         * The grid's content panel.
         */
        private final ContentPanel gridPanel;

        /**
         * The label containing the file's name.
         */
        private final Label nameFieldLabel;

        private final ArrayList<FileDetailsWindowListener> listeners = new ArrayList<FileDetailsWindowListener>();

        /**
         * Builds the window.
         */
        public FileDetailsWindow(final Dispatcher dispatcher) {

            // Creates actions menu to manage the files list.
            final Button downloadButton = new Button(I18N.CONSTANTS.flexibleElementFilesListDownload());
            downloadButton.setEnabled(false);

            // Uses a "hidden form" to manages the downloads to be able to catch
            // server errors.
            final FormPanel downloadFormPanel = new FormPanel();
            downloadFormPanel.setBodyBorder(false);
            downloadFormPanel.setHeaderVisible(false);
            downloadFormPanel.setPadding(0);
            downloadFormPanel.setEncoding(Encoding.URLENCODED);
            downloadFormPanel.setMethod(Method.GET);
            downloadFormPanel.setAction(GWT.getModuleBaseURL() + "download");

            final HiddenField<String> fileIdHidden = new HiddenField<String>();
            fileIdHidden.setName(FileUploadUtils.DOCUMENT_ID);

            final HiddenField<String> versionHidden = new HiddenField<String>();
            versionHidden.setName(FileUploadUtils.DOCUMENT_VERSION);

            downloadFormPanel.add(downloadButton);
            downloadFormPanel.add(fileIdHidden);
            downloadFormPanel.add(versionHidden);

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

            final Button deleteButton = new Button(I18N.CONSTANTS.remove());
            deleteButton.setEnabled(false);

            final ToolBar actionsToolBar = new ToolBar();
            actionsToolBar.add(downloadFormPanel);
            actionsToolBar.add(new SeparatorToolItem());
            actionsToolBar.add(deleteButton);

            // Grid plugins.
            final CheckBoxSelectionModel<FileVersionDTO> selectionModel = new CheckBoxSelectionModel<FileVersionDTO>();
            selectionModel.setSelectionMode(SelectionMode.SINGLE);

            // Builds the versions grid.
            store = new ListStore<FileVersionDTO>();
            final Grid<FileVersionDTO> versionsGrid = new Grid<FileVersionDTO>(store, getColumnModel(selectionModel));
            versionsGrid.setAutoExpandColumn("author");
            versionsGrid.setBorders(false);
            versionsGrid.getView().setForceFit(true);
            versionsGrid.setSelectionModel(selectionModel);
            versionsGrid.addPlugin(selectionModel);

            // Creates the grid main panel.
            gridPanel = new ContentPanel();
            gridPanel.setHeaderVisible(true);
            gridPanel.setLayout(new FitLayout());

            gridPanel.setTopComponent(actionsToolBar);
            gridPanel.add(versionsGrid);

            // Creates the properties panel.
            final ContentPanel propertiesPanel = new ContentPanel();
            propertiesPanel.setHeading(I18N.CONSTANTS.flexibleElementFilesListProperties());
            propertiesPanel.setLayout(new FlowLayout());

            final com.google.gwt.user.client.ui.Grid grid = new com.google.gwt.user.client.ui.Grid(1, 2);
            grid.getColumnFormatter().setWidth(0, "100px");
            grid.setWidth("100%");

            // Displays file's name.
            final Label nameLabel = new Label(I18N.CONSTANTS.flexibleElementFilesListName() + ": ");
            nameLabel.addStyleName("flexibility-element-field-label");
            grid.setWidget(0, 0, nameLabel);
            nameFieldLabel = new Label();
            grid.setWidget(0, 1, nameFieldLabel);
            propertiesPanel.add(grid);

            // Creates the main panel.
            final ContentPanel mainPanel = new ContentPanel();
            mainPanel.setHeaderVisible(false);
            mainPanel.setLayout(new FitLayout());

            mainPanel.setTopComponent(propertiesPanel);
            mainPanel.add(gridPanel, new FitData(new Margins(0, 0, 0, 0)));

            // Builds window.
            window = new Window();
            window.setSize(500, 300);
            window.setPlain(true);
            window.setModal(true);
            window.setBlinkModal(true);
            window.setLayout(new FitLayout());

            window.add(mainPanel);

            // Manages action buttons activations.
            selectionModel.addSelectionChangedListener(new SelectionChangedListener<FileVersionDTO>() {

                @Override
                public void selectionChanged(SelectionChangedEvent<FileVersionDTO> se) {
                    final List<FileVersionDTO> selection = se.getSelection();
                    final boolean enabledState = selection != null && !selection.isEmpty();
                    downloadButton.setEnabled(enabledState);
                    // A version can be deleted only if it isn't the only one.
                    deleteButton.setEnabled(enabledState && file.getVersionsDTO().size() > 1);
                }
            });

            // Buttons listeners.
            downloadButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
                @Override
                public void handleEvent(ButtonEvent be) {

                    // Sets the form hidden fields values.
                    fileIdHidden.setValue(String.valueOf(file.getId()));
                    versionHidden.setValue(String.valueOf(selectionModel.getSelectedItem().getVersionNumber()));

                    // Submits the form.
                    downloadFormPanel.submit();
                }
            });

            deleteButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
                @Override
                public void handleEvent(ButtonEvent be) {

                    // Gets the selected version.
                    final FileVersionDTO version = selectionModel.getSelectedItem();

                    // Asks the client to confirm the version deletion.
                    MessageBox.confirm(I18N.CONSTANTS.flexibleElementFilesListVersionDelete(), I18N.MESSAGES
                            .flexibleElementFilesListConfirmVersionDelete(String.valueOf(version.getVersionNumber())),
                            new Listener<MessageBoxEvent>() {
                                public void handleEvent(MessageBoxEvent ce) {

                                    if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {

                                        // Deletes it.
                                        dispatcher.execute(new Delete(version), new MaskingAsyncMonitor(versionsGrid,
                                                I18N.CONSTANTS.loading()), new AsyncCallback<VoidResult>() {

                                            public void onFailure(Throwable caught) {
                                                MessageBox.alert(I18N.CONSTANTS.flexibleElementFilesListDeleteError(),
                                                        I18N.CONSTANTS.flexibleElementFilesListDeleteErrorDetails(),
                                                        null);
                                            }

                                            public void onSuccess(VoidResult result) {
                                                store.remove(version);
                                                fireVersionDeleted(version);
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
        }

        /**
         * Defines the column model for the versions list grid.
         * 
         * @param selectionModel
         *            The grid's selection model.
         * @return The column model.
         */
        private ColumnModel getColumnModel(CheckBoxSelectionModel<FileVersionDTO> selectionModel) {

            final List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

            columnConfigs.add(selectionModel.getColumn());

            // Version's number.
            ColumnConfig column = new ColumnConfig();
            column.setId("versionNumber");
            column.setHeader(I18N.CONSTANTS.flexibleElementFilesListVersionNumber());
            column.setWidth(55);
            columnConfigs.add(column);

            // Version's add date.
            column = new ColumnConfig();
            column.setId("addedDate");
            column.setHeader(I18N.CONSTANTS.flexibleElementFilesListDate());
            column.setWidth(110);
            column.setDateTimeFormat(DateTimeFormat.getFormat("dd/MM/yyyy HH:mm"));
            columnConfigs.add(column);

            // File's author.
            column = new ColumnConfig();
            column.setId("author");
            column.setHeader(I18N.CONSTANTS.flexibleElementFilesListAuthor());
            column.setWidth(100);
            column.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {
                    return model.getAuthorFirstName() + " " + model.getAuthorName();
                }
            });
            columnConfigs.add(column);

            // Version's size.
            column = new ColumnConfig();
            column.setId("size");
            column.setHeader(I18N.CONSTANTS.flexibleElementFilesListSize());
            column.setWidth(60);
            column.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {
                    final Size size = Size.convertToBestUnit(new Size(model.getSize(), Size.SizeUnit.BYTE));
                    return Math.round(size.getSize()) + " " + Size.SizeUnit.getTranslation(size.getUnit());
                }
            });
            columnConfigs.add(column);

            return new ColumnModel(columnConfigs);
        }

        /**
         * Shows the windows for the given file.
         * 
         * @param file
         *            The file.
         */
        public void show(FileDTO file) {

            if (file == null) {
                return;
            }

            this.file = file;

            // Clears the existing versions.
            store.removeAll();

            // Adds each version to the store to be displayed in the grid.
            int count = 0;
            for (FileVersionDTO version : this.file.getVersionsDTO()) {
                store.add(version);
                count++;
            }

            // Configures the window parameters to be consistent with the new
            // displayed file.
            nameFieldLabel.setText(this.file.getName());
            gridPanel.setHeading(I18N.CONSTANTS.flexibleElementFilesListVersionsList() + " (" + count + ")");
            window.setHeading(I18N.CONSTANTS.flexibleElementFilesListFileDetails() + " - " + this.file.getName());
            window.show();
        }

        public void addListener(FileDetailsWindowListener listener) {
            listeners.add(listener);
        }

        protected void fireVersionDeleted(FileVersionDTO version) {
            for (final FileDetailsWindowListener listener : listeners) {
                listener.versionDeleted(version);
            }
        }
    }

    /**
     * Utility class used to manipulate file's sizes.
     * 
     * @author tmi
     * 
     */
    public static final class Size {

        private final double size;

        private final SizeUnit unit;

        public Size(double size, SizeUnit unit) {
            this.size = size;
            this.unit = unit;
        }

        public double getSize() {
            return size;
        }

        public SizeUnit getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(size);
            sb.append(unit.name());
            return sb.toString();
        }

        /**
         * Converts a size from one unit to another.
         * 
         * @param size
         *            The size (must be <code>positive</code> and not
         *            <code>null</code>).
         * @param targetUnit
         *            The desired size unit.
         * @return The size converted.
         */
        public static Size convert(Size size, SizeUnit targetUnit) {

            if (targetUnit == null) {
                throw new IllegalArgumentException("Units cannot be null.");
            }

            if (size == null) {
                throw new IllegalArgumentException("Size cannot be null.");
            }

            if (size.size < 0) {
                throw new IllegalArgumentException("Size cannot be negative.");
            }

            if (size.size == 0 || size.unit == targetUnit) {
                return size;
            }

            double computedSize;

            if (size.unit.weight < targetUnit.weight) {
                computedSize = size.size / ((targetUnit.weight - size.unit.weight) * SizeUnit.STEP);
            } else {
                computedSize = size.size * Math.pow(SizeUnit.STEP, size.unit.weight - targetUnit.weight);
            }

            return new Size(computedSize, targetUnit);
        }

        /**
         * Converts a size to the best appropriate unit (greater than
         * <code>0</code>).
         * 
         * @param size
         *            The size (must be <code>positive</code> and not
         *            <code>null</code>).
         * @return
         */
        public static Size convertToBestUnit(Size size) {

            if (size == null) {
                throw new IllegalArgumentException("Size cannot be null.");
            }

            if (size.size < 0) {
                throw new IllegalArgumentException("Size cannot be negative.");
            }

            if (size.size == 0) {
                return size;
            }

            double computedSize = 0;
            SizeUnit computedUnit = null;

            double currentSize = size.size;
            SizeUnit currentUnit = size.unit;

            if (size.size > 1) {

                while (currentSize >= 1 && currentUnit != null) {

                    computedSize = currentSize;
                    computedUnit = currentUnit;

                    currentSize = currentSize / SizeUnit.STEP;
                    currentUnit = SizeUnit.getPrevUnit(currentUnit);
                }

            } else {

                computedSize = size.size;
                computedUnit = size.unit;

                while (computedSize <= 1 && currentUnit != null) {

                    computedSize = currentSize;
                    computedUnit = currentUnit;

                    currentSize = currentSize * SizeUnit.STEP;
                    currentUnit = SizeUnit.getNextUnit(currentUnit);
                }
            }

            return new Size(computedSize, computedUnit);
        }

        /**
         * Represents size units.
         * 
         * @author tmi
         * 
         */
        public static enum SizeUnit {

            BYTE(1), KILOBYTE(2), MEGABYTE(3), GIGABYTE(4), TERABYTE(5);

            private static final int STEP = 1024;

            private final int weight;

            private SizeUnit(int weight) {
                this.weight = weight;
            }

            /**
             * Gets the next unit (the first greater one).
             * 
             * @param unit
             *            The base unit.
             * @return The next unit.
             */
            private static SizeUnit getNextUnit(SizeUnit unit) {

                if (unit == null) {
                    return null;
                }

                for (final SizeUnit u : SizeUnit.values()) {
                    if (u.weight + 1 == unit.weight) {
                        return u;
                    }
                }

                return null;
            }

            /**
             * Gets the previous unit (the first lower one).
             * 
             * @param unit
             *            The base unit.
             * @return The previous unit.
             */
            private static SizeUnit getPrevUnit(SizeUnit unit) {

                if (unit == null) {
                    return null;
                }

                for (final SizeUnit u : SizeUnit.values()) {
                    if (u.weight - 1 == unit.weight) {
                        return u;
                    }
                }

                return null;
            }

            /**
             * Gets the translation key of this unit specific to the current
             * application.
             * 
             * @param unit
             *            The unit.
             * @return The translation key.
             */
            public static String getTranslation(SizeUnit unit) {

                switch (unit) {
                case BYTE:
                    return I18N.CONSTANTS.flexibleElementFilesListSizeByteUnit();
                case KILOBYTE:
                    return I18N.CONSTANTS.flexibleElementFilesListSizeKByteUnit();
                case MEGABYTE:
                    return I18N.CONSTANTS.flexibleElementFilesListSizeMByteUnit();
                case GIGABYTE:
                    return I18N.CONSTANTS.flexibleElementFilesListSizeGByteUnit();
                case TERABYTE:
                    return I18N.CONSTANTS.flexibleElementFilesListSizeTByteUnit();
                default:
                    return "";
                }
            }
        }
    }
}
