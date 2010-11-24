/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dto.element;

import java.io.Serializable;
import java.util.ArrayList;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.ui.ButtonFileUploadField;
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
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

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
     * The component main panel.
     */
    private transient ContentPanel mainPanel;

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
            store.sort("date", SortDir.DESC);
        }
    }

    @Override
    public void init() {
        super.init();
        store = new ListStore<FileDTO>();
    }

    @Override
    public Component getComponent(ValueResult valueResult, boolean enabled) {

        currentValueResult = valueResult;

        // Creates the upload button (with a hidden form panel).
        final ButtonFileUploadField uploadField = new ButtonFileUploadField();
        uploadField.setButtonCaption(I18N.CONSTANTS.flexibleElementFilesListAddDocument());
        uploadField.setName(FileUploadUtils.DOCUMENT_CONTENT);
        uploadField.setButtonIcon(IconImageBundle.ICONS.attach());
        uploadField.setEnabled(enabled);

        final FormPanel uploadFormPanel = new FormPanel();
        uploadFormPanel.setLayout(new FitLayout());
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

        uploadFormPanel.add(uploadField);
        uploadFormPanel.add(nameHidden);
        uploadFormPanel.add(authorHidden);
        uploadFormPanel.add(elementIdHidden);
        uploadFormPanel.add(projectIdHidden);
        uploadFormPanel.add(emptyHidden);

        // Creates actions tool bar.
        final ToolBar actionsToolBar = new ToolBar();
        actionsToolBar.setAlignment(HorizontalAlignment.LEFT);

        actionsToolBar.add(uploadFormPanel);

        // Upload the selected file immediately after it's selected.
        uploadField.addListener(Events.OnChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                mainPanel.mask(I18N.CONSTANTS.loading());

                // Set hidden fields values.
                elementIdHidden.setValue(String.valueOf(getId()));
                projectIdHidden.setValue(String.valueOf(currentContainerDTO.getId()));
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
                    sb.append(" ; project id=");
                    sb.append(projectIdHidden.getValue());
                    sb.append(" ; element id=");
                    sb.append(elementIdHidden.getValue());
                    sb.append(" ; allow empty=");
                    sb.append(emptyHidden.getValue());

                    Log.debug(sb.toString());
                }

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
                uploadField.reset();

                handlerManager.fireEvent(new RequiredValueEvent(true));

                mainPanel.unmask();
            }
        });

        updateStore();

        // Creates the grid which contains the files list.
        final FlexibleGrid<FileDTO> filesGrid = new FlexibleGrid<FileDTO>(store, null, getColumnModel(enabled));
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
                } else if ("name".equals(property)) {

                    final FileVersionDTO last1 = m1.getLastVersion();
                    final FileVersionDTO last2 = m2.getLastVersion();

                    final String title1 = last1.getName() + '.' + last1.getExtension();
                    final String title2 = last2.getName() + '.' + last2.getExtension();

                    return title1.compareTo(title2);
                }

                else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });

        // Creates the main panel.
        mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(true);
        mainPanel.setBorders(true);
        mainPanel.setHeading(getLabel());

        mainPanel.setTopComponent(actionsToolBar);
        mainPanel.add(filesGrid);

        return mainPanel;
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
        final GetValue command = new GetValue(currentContainerDTO.getId(), getId(), getEntityName());

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
     * @param enabled
     *            If the component is enabled.
     * @return The column model.
     */
    private ColumnConfig[] getColumnModel(final boolean enabled) {

        // File's add date.
        final ColumnConfig dateColumn = new ColumnConfig();
        dateColumn.setId("date");
        dateColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListDate());
        dateColumn.setWidth(60);
        dateColumn.setRenderer(new GridCellRenderer<FileDTO>() {

            final DateTimeFormat format = DateTimeFormat.getFormat("dd/MM/yyyy");

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
        nameColumn.setWidth(100);
        nameColumn.setRenderer(new GridCellRenderer<FileDTO>() {

            @Override
            public Object render(final FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {
                return createDownloadLink(model, null);
            }
        });

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
        final ColumnConfig versionColumn = new ColumnConfig();
        versionColumn.setId("version");
        versionColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListVersion());
        versionColumn.setWidth(20);
        versionColumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(final FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {
                final FileVersionDTO last = model.getLastVersion();
                return last.getVersionNumber();
            }
        });

        // Upload new version.
        final ColumnConfig addVersionColumn = new ColumnConfig();
        addVersionColumn.setId("addVersion");
        addVersionColumn.setHeader(null);
        addVersionColumn.setWidth(60);
        addVersionColumn.setSortable(false);
        addVersionColumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(final FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {

                final ButtonFileUploadField uploadField = new ButtonFileUploadField();
                uploadField.setButtonCaption(I18N.CONSTANTS.flexibleElementFilesListUploadVersion());
                uploadField.setName(FileUploadUtils.DOCUMENT_CONTENT);
                uploadField.setButtonIcon(IconImageBundle.ICONS.attach());
                uploadField.setEnabled(enabled);

                final FormPanel uploadFormPanel = new FormPanel();
                uploadFormPanel.setLayout(new FitLayout());
                uploadFormPanel.setBodyBorder(false);
                uploadFormPanel.setHeaderVisible(false);
                uploadFormPanel.setPadding(0);
                uploadFormPanel.setEncoding(Encoding.MULTIPART);
                uploadFormPanel.setMethod(Method.POST);
                uploadFormPanel.setAction(GWT.getModuleBaseURL() + "upload");

                final HiddenField<String> idHidden = new HiddenField<String>();
                idHidden.setName(FileUploadUtils.DOCUMENT_ID);

                final HiddenField<String> nameHidden = new HiddenField<String>();
                nameHidden.setName(FileUploadUtils.DOCUMENT_NAME);

                final HiddenField<String> authorHidden = new HiddenField<String>();
                authorHidden.setName(FileUploadUtils.DOCUMENT_AUTHOR);

                final HiddenField<String> versionHidden = new HiddenField<String>();
                versionHidden.setName(FileUploadUtils.DOCUMENT_VERSION);

                final HiddenField<String> emptyHidden = new HiddenField<String>();
                emptyHidden.setName(FileUploadUtils.CHECK_EMPTY);

                uploadFormPanel.add(uploadField);
                uploadFormPanel.add(authorHidden);
                uploadFormPanel.add(idHidden);
                uploadFormPanel.add(nameHidden);
                uploadFormPanel.add(versionHidden);
                uploadFormPanel.add(emptyHidden);

                uploadField.addListener(Events.OnChange, new Listener<BaseEvent>() {

                    @Override
                    public void handleEvent(BaseEvent be) {

                        // Set hidden fields values.
                        idHidden.setValue(String.valueOf(model.getId()));
                        nameHidden.setValue(uploadField.getValue());
                        authorHidden.setValue(String.valueOf(authentication.getUserId()));
                        versionHidden.setValue(String.valueOf(model.getLastVersion().getVersionNumber() + 1));
                        emptyHidden.setValue("true");

                        // Debug form hidden values.
                        if (Log.isDebugEnabled()) {

                            final StringBuilder sb = new StringBuilder();
                            sb.append("Upload a new version with parameters: ");
                            sb.append("version number=");
                            sb.append(versionHidden.getValue());
                            sb.append(" ; file id=");
                            sb.append(idHidden.getValue());
                            sb.append(" ; file name=");
                            sb.append(nameHidden.getValue());
                            sb.append(" ; author id=");
                            sb.append(authorHidden.getValue());
                            sb.append(" ; allow empty=");
                            sb.append(emptyHidden.getValue());

                            Log.debug(sb.toString());
                        }

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
                        uploadField.reset();
                    }
                });

                return uploadFormPanel;
            }
        });

        // Versions list.
        final ColumnConfig historyColumn = new ColumnConfig();
        historyColumn.setId("history");
        historyColumn.setHeader(null);
        historyColumn.setWidth(20);
        historyColumn.setSortable(false);
        historyColumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(final FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<FileDTO> store, Grid<FileDTO> grid) {

                final com.google.gwt.user.client.ui.Label historyButton = new com.google.gwt.user.client.ui.Label(
                        I18N.CONSTANTS.flexibleElementFilesListHistory());
                historyButton.addStyleName("flexibility-action");
                historyButton.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent e) {

                        final FileDetailsWindow versionsWindow = new FileDetailsWindow(dispatcher, enabled);
                        versionsWindow.addListener(new FileDetailsWindow.FileDetailsWindowListener() {
                            @Override
                            public void versionDeleted(FileVersionDTO version) {
                                updateComponent();
                            }
                        });

                        versionsWindow.show(model);
                    }
                });

                return historyButton;
            }
        });

        // Delete.
        final ColumnConfig deleteColumn = new ColumnConfig();
        deleteColumn.setId("delete");
        deleteColumn.setHeader(null);
        deleteColumn.setWidth(10);
        deleteColumn.setSortable(false);
        deleteColumn.setRenderer(new GridCellRenderer<FileDTO>() {
            @Override
            public Object render(final FileDTO model, String property, ColumnData config, int rowIndex, int colIndex,
                    final ListStore<FileDTO> store, Grid<FileDTO> grid) {

                if (enabled) {
                    final Image image = IconImageBundle.ICONS.remove().createImage();
                    image.setTitle(I18N.CONSTANTS.remove());
                    image.addStyleName("flexibility-action");
                    image.addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {

                            // Asks the client to confirm the file removal.
                            MessageBox.confirm(I18N.CONSTANTS.flexibleElementFilesListDelete(),
                                    I18N.MESSAGES.flexibleElementFilesListConfirmDelete(model.getName()),
                                    new Listener<MessageBoxEvent>() {
                                        @Override
                                        public void handleEvent(MessageBoxEvent ce) {

                                            if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {

                                                // Deletes it.
                                                dispatcher.execute(new Delete(model), new MaskingAsyncMonitor(
                                                        mainPanel, I18N.CONSTANTS.loading()),
                                                        new AsyncCallback<VoidResult>() {

                                                            public void onFailure(Throwable caught) {
                                                                MessageBox.alert(
                                                                        I18N.CONSTANTS
                                                                                .flexibleElementFilesListDeleteError(),
                                                                        I18N.CONSTANTS
                                                                                .flexibleElementFilesListDeleteErrorDetails(),
                                                                        null);
                                                            }

                                                            public void onSuccess(VoidResult result) {
                                                                store.remove(model);
                                                                if (store.getCount() == 0) {
                                                                    handlerManager.fireEvent(new RequiredValueEvent(
                                                                            false));
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }
                    });

                    return image;
                } else {
                    return new Label("-");
                }
            }
        });

        return new ColumnConfig[] { dateColumn, nameColumn, authorColumn, versionColumn, addVersionColumn,
                historyColumn, deleteColumn };
    }

    /**
     * Builds and shows a window with the file's details.
     * 
     * @author tmi
     * 
     */
    private static final class FileDetailsWindow {

        /**
         * Listener.
         * 
         * @author tmi
         * 
         */
        private interface FileDetailsWindowListener {

            /**
             * Method called when a version is deleted.
             * 
             * @param version
             *            The deleted version.
             */
            public void versionDeleted(FileVersionDTO version);
        }

        private final Dispatcher dispatcher;

        /**
         * GXT window.
         */
        private final Window window;

        /**
         * Window main panel.
         */
        private final ContentPanel mainPanel;

        /**
         * The versiions grid.
         */
        private final FlexibleGrid<FileVersionDTO> grid;

        /**
         * The current displayed file.
         */
        private FileDTO file;

        /**
         * Versions store.
         */
        private final ListStore<FileVersionDTO> store;

        /**
         * Listeners
         */
        private final ArrayList<FileDetailsWindowListener> listeners = new ArrayList<FileDetailsWindowListener>();

        /**
         * Builds the window.
         * 
         * @param dispatcher
         * @param enabled
         *            If the component is enabled.
         */
        public FileDetailsWindow(final Dispatcher dispatcher, boolean enabled) {

            this.dispatcher = dispatcher;

            store = new ListStore<FileVersionDTO>();

            grid = new FlexibleGrid<FileVersionDTO>(store, null, 10, getColumnModel(enabled));
            grid.setAutoExpandColumn("name");

            store.setStoreSorter(new StoreSorter<FileVersionDTO>() {
                @Override
                public int compare(Store<FileVersionDTO> store, FileVersionDTO m1, FileVersionDTO m2, String property) {

                    if ("author".equals(property)) {

                        final String authorM1 = m1.getAuthorFirstName() != null ? m1.getAuthorFirstName() + " "
                                + m1.getAuthorName() : m1.getAuthorName();
                        final String authorM2 = m2.getAuthorFirstName() != null ? m2.getAuthorFirstName() + " "
                                + m2.getAuthorName() : m2.getAuthorName();

                        return authorM1.compareTo(authorM2);
                    } else if ("name".equals(property)) {

                        final String title1 = m1.getName() + '.' + m1.getExtension();
                        final String title2 = m2.getName() + '.' + m2.getExtension();

                        return title1.compareTo(title2);
                    } else if ("size".equals(property)) {

                        return new Long(m1.getSize()).compareTo(m2.getSize());
                    }

                    else {
                        return super.compare(store, m1, m2, property);
                    }
                }
            });

            mainPanel = new ContentPanel();
            mainPanel.setHeaderVisible(false);
            mainPanel.setBodyBorder(false);
            mainPanel.add(grid);
            mainPanel.setScrollMode(Scroll.AUTOY);

            // Builds window.
            window = new Window();
            window.setSize(550, 250);
            window.setPlain(true);
            window.setModal(true);
            window.setBlinkModal(true);
            window.setResizable(false);
            window.setLayout(new FitLayout());

            window.add(mainPanel);
        }

        /**
         * Defines the column model for the versions list grid.
         * 
         * @param enabled
         *            If the component is enabled.
         * @return The column model.
         */
        private ColumnConfig[] getColumnModel(final boolean enabled) {

            // Version's number.
            final ColumnConfig versionColumn = new ColumnConfig();
            versionColumn.setId("versionNumber");
            versionColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListVersionNumber());
            versionColumn.setWidth(55);

            // Version's add date.
            final ColumnConfig dateColumn = new ColumnConfig();
            dateColumn.setId("addedDate");
            dateColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListDate());
            dateColumn.setWidth(110);
            dateColumn.setDateTimeFormat(DateTimeFormat.getFormat("dd/MM/yyyy HH:mm"));

            // Version's author.
            final ColumnConfig authorColumn = new ColumnConfig();
            authorColumn.setId("author");
            authorColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListAuthor());
            authorColumn.setWidth(100);
            authorColumn.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {
                    return model.getAuthorFirstName() != null ? model.getAuthorFirstName() + " "
                            + model.getAuthorName() : model.getAuthorName();
                }
            });

            // Version's name.
            final ColumnConfig nameColumn = new ColumnConfig();
            nameColumn.setId("name");
            nameColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListName());
            nameColumn.setWidth(100);
            nameColumn.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {
                    return createDownloadLink(file, model.getVersionNumber());
                }
            });

            // Version's size.
            final ColumnConfig sizeColumn = new ColumnConfig();
            sizeColumn.setId("size");
            sizeColumn.setHeader(I18N.CONSTANTS.flexibleElementFilesListSize());
            sizeColumn.setWidth(60);
            sizeColumn.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {
                    final Size size = Size.convertToBestUnit(new Size(model.getSize(), Size.SizeUnit.BYTE));
                    return Math.round(size.getSize()) + " " + Size.SizeUnit.getTranslation(size.getUnit());
                }
            });

            // Delete.
            final ColumnConfig deleteColumn = new ColumnConfig();
            deleteColumn.setId("delete");
            deleteColumn.setHeader(null);
            deleteColumn.setWidth(25);
            deleteColumn.setSortable(false);
            deleteColumn.setRenderer(new GridCellRenderer<FileVersionDTO>() {
                @Override
                public Object render(final FileVersionDTO model, String property, ColumnData config, int rowIndex,
                        int colIndex, final ListStore<FileVersionDTO> store, Grid<FileVersionDTO> grid) {

                    if (enabled) {
                        final Image image = IconImageBundle.ICONS.remove().createImage();
                        image.setTitle(I18N.CONSTANTS.remove());
                        image.addStyleName("flexibility-action");
                        image.addClickHandler(new ClickHandler() {

                            @Override
                            public void onClick(ClickEvent event) {

                                // Do not delete a single version.
                                if (store.getCount() <= 1) {
                                    MessageBox.alert(I18N.CONSTANTS.flexibleElementFilesListVersionDeleteForbidden(),
                                            I18N.CONSTANTS.flexibleElementFilesListVersionDeleteForbiddenDetails(),
                                            null);
                                    return;
                                }

                                // Asks the client to confirm the version
                                // deletion.
                                MessageBox.confirm(I18N.CONSTANTS.flexibleElementFilesListVersionDelete(),
                                        I18N.MESSAGES.flexibleElementFilesListConfirmVersionDelete(String.valueOf(model
                                                .getVersionNumber())), new Listener<MessageBoxEvent>() {
                                            public void handleEvent(MessageBoxEvent ce) {

                                                if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {

                                                    // Deletes it.
                                                    dispatcher.execute(new Delete(model), new MaskingAsyncMonitor(
                                                            window, I18N.CONSTANTS.loading()),
                                                            new AsyncCallback<VoidResult>() {

                                                                public void onFailure(Throwable caught) {
                                                                    MessageBox.alert(
                                                                            I18N.CONSTANTS
                                                                                    .flexibleElementFilesListDeleteError(),
                                                                            I18N.CONSTANTS
                                                                                    .flexibleElementFilesListDeleteErrorDetails(),
                                                                            null);
                                                                }

                                                                public void onSuccess(VoidResult result) {
                                                                    store.remove(model);
                                                                    fireVersionDeleted(model);
                                                                }
                                                            });
                                                }
                                            }
                                        });

                            }
                        });

                        return image;
                    } else {
                        return new Label("-");
                    }
                }
            });

            return new ColumnConfig[] { versionColumn, dateColumn, authorColumn, nameColumn, sizeColumn, deleteColumn };
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
            final FileVersionDTO lastVersion = file.getLastVersion();

            // Clears the existing versions.
            store.removeAll();

            // Adds each version to the store to be displayed in the grid.
            for (final FileVersionDTO version : this.file.getVersionsDTO()) {
                store.add(version);
            }

            store.sort("versionNumber", SortDir.DESC);

            // Configures the window parameters to be consistent with the new
            // displayed file.
            window.setHeading(I18N.CONSTANTS.flexibleElementFilesListHistory() + " - " + lastVersion.getName() + '.'
                    + lastVersion.getExtension());
            window.show();
        }

        /**
         * Adds a listener to the window.
         * 
         * @param listener
         *            The new listener.
         */
        public void addListener(FileDetailsWindowListener listener) {
            listeners.add(listener);
        }

        /**
         * Method called when a version is deleted.
         * 
         * @param version
         *            The deleted version.
         */
        protected void fireVersionDeleted(FileVersionDTO version) {
            for (final FileDetailsWindowListener listener : listeners) {
                listener.versionDeleted(version);
            }
        }
    }

    /**
     * Create a download link for the given file.
     * 
     * @param file
     *            The file to download.
     * @return The download link.
     */
    private static Component createDownloadLink(final FileDTO file, final Integer version) {

        final FileVersionDTO versionDTO;
        if (version != null) {
            versionDTO = file.getVersion(version);
        } else {
            versionDTO = file.getLastVersion();
        }

        if (versionDTO == null) {
            return null;
        }

        // Uses a "hidden form" to manages the downloads to be able to
        // catch server errors.
        final FormPanel downloadFormPanel = new FormPanel();
        downloadFormPanel.setBodyBorder(false);
        downloadFormPanel.setHeaderVisible(false);
        downloadFormPanel.setPadding(0);
        downloadFormPanel.setEncoding(Encoding.URLENCODED);
        downloadFormPanel.setMethod(Method.GET);
        downloadFormPanel.setAction(GWT.getModuleBaseURL() + "download");

        final com.google.gwt.user.client.ui.Label downloadButton = new com.google.gwt.user.client.ui.Label(
                versionDTO.getName() + '.' + versionDTO.getExtension());
        downloadButton.addStyleName("flexibility-action");

        // File's id.
        final HiddenField<String> fileIdHidden = new HiddenField<String>();
        fileIdHidden.setName(FileUploadUtils.DOCUMENT_ID);
        fileIdHidden.setValue(String.valueOf(file.getId()));

        downloadFormPanel.add(downloadButton);
        downloadFormPanel.add(fileIdHidden);

        // File's version if any.
        if (version != null) {

            final HiddenField<String> versionHidden = new HiddenField<String>();
            versionHidden.setName(FileUploadUtils.DOCUMENT_VERSION);
            versionHidden.setValue(String.valueOf(version));

            downloadFormPanel.add(versionHidden);
        }

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
