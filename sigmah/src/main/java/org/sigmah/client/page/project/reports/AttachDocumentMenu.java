package org.sigmah.client.page.project.reports;

import java.util.Date;
import java.util.List;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.project.logframe.FormWindow;
import org.sigmah.client.page.project.logframe.FormWindow.FormSubmitListener;
import org.sigmah.client.ui.ButtonFileUploadField;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.GetProjectReports;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectDTO.LocalizedElement;
import org.sigmah.shared.dto.element.FilesListElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.reminder.MonitoredPointDTO;
import org.sigmah.shared.dto.value.FileUploadUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FormEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.core.client.GWT;

/**
 * Builds a menu to attach documents among a project.
 * 
 * @author tmi
 * 
 */
public class AttachDocumentMenu {

    private final Authentication authentication;
    private final ProjectDTO project;
    private boolean monitoredPointGenerated;
    private Dialog dialog;
    private FormPanel uploadFormPanel;
    private LabelField phaseField;
    private LabelField elementField;
    private FlexibleElementDTO currentFlexibleElement;
    private final ListStore<GetProjectReports.ReportReference> documentsStore;
    private GetProjectReports.ReportReference currentCreatedDocument;

    /**
     * Creates a menu for attaching documents.
     * 
     * @param authentication
     *            The authentication.
     * @param project
     *            The current project.
     * @param button
     *            The button at which this menu is attached.
     */
    public AttachDocumentMenu(Authentication authentication, ProjectDTO project, Button button,
            ListStore<GetProjectReports.ReportReference> documentsStore) {

        this.authentication = authentication;
        this.project = project;
        this.documentsStore = documentsStore;

        boolean enabled = false;

        // Builds the menu.
        final Menu menu = new Menu();

        // Retrieves all the files lists elements in the current project.
        final List<LocalizedElement> filesLists = project.getLocalizedElements(FilesListElementDTO.class);

        if (filesLists != null) {
            // For each files list.
            for (final LocalizedElement filesList : filesLists) {

                // Builds the item label.
                final StringBuilder sb = new StringBuilder();
                if (filesList.getPhaseModel() != null) {
                    sb.append(filesList.getPhaseModel().getName());
                } else {
                    sb.append(I18N.CONSTANTS.projectDetails());
                }
                sb.append(" | ");
                sb.append(filesList.getElement().getLabel());

                // Builds the corresponding menu item.
                final MenuItem item = new MenuItem(sb.toString());

                // If the phase is the details page.
                if (filesList.getPhase() == null) {
                    item.addSelectionListener(getAttachListener(I18N.CONSTANTS.projectDetails(), filesList.getElement()));
                    enabled = true;
                }
                // If the phase is closed.
                else if (filesList.getPhase().isEnded()) {
                    item.setEnabled(false);
                    item.setIcon(IconImageBundle.ICONS.close());
                    item.setTitle(I18N.CONSTANTS.flexibleElementFilesListAddErrorPhaseClosed());
                }
                // If the phase is active.
                else if (filesList.getPhase() == project.getCurrentPhaseDTO()) {
                    item.setIcon(IconImageBundle.ICONS.activate());
                    item.addSelectionListener(getAttachListener(filesList.getPhaseModel().getName(),
                            filesList.getElement()));
                    enabled = true;
                }
                // If the phase is a successor of the active one.
                else if (filesList.getPhase().isSuccessor(project.getCurrentPhaseDTO())) {
                    item.addSelectionListener(getAttachListener(filesList.getPhaseModel().getName(),
                            filesList.getElement()));
                    enabled = true;
                }
                // Future phase, not yet accessible.
                else {
                    item.setEnabled(false);
                    item.setTitle(I18N.CONSTANTS.flexibleElementFilesListAddErrorPhaseInactive());
                }

                menu.add(item);
            }
        }

        // Adds this menu to the button.
        if (enabled) {
            button.setEnabled(true);
            button.setMenu(menu);
        } else {
            button.setEnabled(false);
        }
    }

    /**
     * Builds the dialog box.
     */
    private void createDialog() {

        // Phase name field.
        phaseField = new LabelField();
        phaseField.setFieldLabel(I18N.CONSTANTS.reportPhase());

        // Flexible element label field.
        elementField = new LabelField();
        elementField.setFieldLabel(I18N.CONSTANTS.flexibleElementFilesList());

        // Creates the upload button (with a hidden form panel).
        final ButtonFileUploadField uploadField = new ButtonFileUploadField();
        uploadField.setButtonCaption(I18N.CONSTANTS.flexibleElementFilesListAddDocument());
        uploadField.setName(FileUploadUtils.DOCUMENT_CONTENT);
        uploadField.setButtonIcon(IconImageBundle.ICONS.attach());

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

        final HiddenField<String> pointDateHidden = new HiddenField<String>();
        pointDateHidden.setName(FileUploadUtils.MONITORED_POINT_DATE);

        final HiddenField<String> pointLabelHidden = new HiddenField<String>();
        pointLabelHidden.setName(FileUploadUtils.MONITORED_POINT_LABEL);

        uploadFormPanel = new FormPanel() {

            @Override
            public void reset() {
                phaseField.reset();
                elementField.reset();
                nameHidden.reset();
                authorHidden.reset();
                elementIdHidden.reset();
                projectIdHidden.reset();
                emptyHidden.reset();
                pointDateHidden.reset();
                pointLabelHidden.reset();
            }
        };

        uploadFormPanel.setLayout(new FitLayout());
        uploadFormPanel.setBodyBorder(false);
        uploadFormPanel.setHeaderVisible(false);
        uploadFormPanel.setPadding(0);
        uploadFormPanel.setEncoding(Encoding.MULTIPART);
        uploadFormPanel.setMethod(Method.POST);
        uploadFormPanel.setAction(GWT.getModuleBaseURL() + "upload");

        uploadFormPanel.add(uploadField);
        uploadFormPanel.add(nameHidden);
        uploadFormPanel.add(authorHidden);
        uploadFormPanel.add(elementIdHidden);
        uploadFormPanel.add(projectIdHidden);
        uploadFormPanel.add(emptyHidden);
        uploadFormPanel.add(pointDateHidden);
        uploadFormPanel.add(pointLabelHidden);

        // Upload the selected file immediately after it's selected.
        uploadField.addListener(Events.OnChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {

                dialog.mask(I18N.CONSTANTS.loading());

                // Set hidden fields values.
                elementIdHidden.setValue(String.valueOf(currentFlexibleElement.getId()));
                projectIdHidden.setValue(String.valueOf(project.getId()));
                nameHidden.setValue(uploadField.getValue());
                authorHidden.setValue(String.valueOf(authentication.getUserId()));
                emptyHidden.setValue("true");

                // Create the local document.
                currentCreatedDocument = new GetProjectReports.ReportReference();
                currentCreatedDocument.setName(uploadField.getValue());
                currentCreatedDocument.setLastEditDate(new Date());
                currentCreatedDocument.setEditorName(authentication.getUserShortName());
                currentCreatedDocument.setDocument(true);
                currentCreatedDocument.setFlexibleElementLabel(elementField.getText());
                currentCreatedDocument.setPhaseName(phaseField.getText());

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

                MessageBox.confirm(I18N.CONSTANTS.monitoredPointAdd(),
                        I18N.MESSAGES.monitoredPointAddWithFile(uploadField.getValue()),
                        new Listener<MessageBoxEvent>() {

                            // Builds the monitored points window.
                            final FormWindow window = new FormWindow();

                            {
                                window.addTextField(I18N.CONSTANTS.monitoredPointLabel(), false);
                                window.addDateField(I18N.CONSTANTS.monitoredPointExpectedDate(), false);

                                window.addFormSubmitListener(new FormSubmitListener() {

                                    @Override
                                    public void formSubmitted(Object... values) {

                                        // Checks that the values are
                                        // correct.
                                        final Object element0 = values[1];
                                        if (!(element0 instanceof Date)) {
                                            return;
                                        }

                                        final Object element1 = values[0];
                                        if (!(element1 instanceof String)) {
                                            return;
                                        }

                                        final Date pointExpectedDate = (Date) element0;
                                        final String pointLabel = (String) element1;

                                        pointDateHidden.setValue(String.valueOf(pointExpectedDate.getTime()));
                                        pointLabelHidden.setValue(pointLabel);

                                        // Submits the form.
                                        uploadFormPanel.submit();
                                    }
                                });
                            }

                            @Override
                            public void handleEvent(MessageBoxEvent be) {

                                if (Dialog.YES.equals(be.getButtonClicked().getItemId())) {
                                    monitoredPointGenerated = true;
                                    window.show(I18N.CONSTANTS.monitoredPointAdd(),
                                            I18N.CONSTANTS.monitoredPointAddDetails());
                                } else {
                                    pointDateHidden.setValue(null);
                                    pointLabelHidden.setValue(null);
                                    // Submits the form.
                                    uploadFormPanel.submit();
                                }
                            }
                        });
            }
        });

        uploadFormPanel.addListener(Events.Submit, new Listener<FormEvent>() {

            @Override
            public void handleEvent(FormEvent be) {

                // Updates the widget for the new value.
                updateComponentAfterUpload(be);

                // Reset upload fields.
                uploadField.reset();

                dialog.unmask();
            }
        });

        // Dialog box.
        dialog = new Dialog();
        dialog.setButtons(Dialog.CLOSE);
        dialog.setHeading(I18N.CONSTANTS.flexibleElementFilesListAddDocumentDetails());
        dialog.setModal(true);

        dialog.setResizable(false);
        dialog.setWidth("340px");

        dialog.setLayout(new FormLayout());
        dialog.add(phaseField);
        dialog.add(elementField);
        dialog.add(uploadFormPanel);
    }

    /**
     * Create a attach listener to manage the file upload.
     * 
     * @return The listener.
     */
    private SelectionListener<MenuEvent> getAttachListener(final String phaseName, final FlexibleElementDTO element) {

        return new SelectionListener<MenuEvent>() {

            @Override
            public void componentSelected(MenuEvent ce) {

                currentFlexibleElement = element;

                if (dialog == null) {
                    createDialog();
                }

                uploadFormPanel.reset();
                phaseField.setText(phaseName);
                elementField.setText(currentFlexibleElement.getLabel());
                dialog.show();
            }
        };
    }

    /**
     * Update files list after an upload.
     * 
     * @param be
     *            Form event after the upload.
     */
    private void updateComponentAfterUpload(FormEvent be) {

        final String code = FileUploadUtils.parseUploadResultCode(be.getResultHtml());

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

            MessageBox.alert(I18N.CONSTANTS.flexibleElementFilesListUploadError(), sb.toString(), null);
        } else {

            dialog.hide();

            currentCreatedDocument.setId(Integer.valueOf(code));

            Notification.show(I18N.CONSTANTS.infoConfirmation(),
                    I18N.CONSTANTS.flexibleElementFilesListUploadFileConfirm());

            // Adds the monitored point.
            if (monitoredPointGenerated) {

                final MonitoredPointDTO point = FileUploadUtils.parseUploadResultMonitoredPoint(be.getResultHtml());

                if (point != null) {
                    if (Log.isDebugEnabled()) {
                        Log.debug("[updateComponentAfterUpload] Adds a monitored point '" + point.getLabel() + "'");
                    }

                    project.addMonitoredPoint(point);
                }

                monitoredPointGenerated = false;
            }

            updateComponent();
        }

        currentCreatedDocument = null;
    }

    /**
     * Refreshes files list
     */
    private void updateComponent() {
        documentsStore.add(currentCreatedDocument);
    }
}
