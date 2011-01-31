/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.reports;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.dto.report.ReportReference;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectDTO.LocalizedElement;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.ReportElementDTO;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class AttachReportHandler implements AttachMenuBuilder.AttachDocumentHandler {
    private Dialog dialog;

    /**
     * Creates a new and empty "Attach Report" dialog.
     * @return The new dialog.
     */
    private Dialog createDialog() {
        final Dialog dialogBox = new Dialog();
        dialogBox.setButtons(Dialog.OKCANCEL);
        dialogBox.setHeading(I18N.CONSTANTS.reportCreateReport());
        dialogBox.setModal(true);

        dialogBox.setResizable(false);
        dialogBox.setWidth("340px");

        dialogBox.setLayout(new FormLayout());

        // Report name
        final TextField<String> nameField = new TextField<String>();
        nameField.setFieldLabel(I18N.CONSTANTS.reportName());
        nameField.setAllowBlank(false);
        nameField.setName("name");
        dialogBox.add(nameField);

        // Parent element name
        final LabelField elementNameField = new LabelField();
        elementNameField.setFieldLabel(I18N.CONSTANTS.flexibleElementReport());
        dialogBox.add(elementNameField);

        // Cancel button action (closes the dialog box)
        dialogBox.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                dialogBox.hide();
            }
        });

        return dialogBox;
    }

    private TextField<String> getNameField(Dialog dialog) {
        return (TextField<String>) dialog.getWidget(0);
    }

    private LabelField getElementNameField(Dialog dialog) {
        return (LabelField) dialog.getWidget(1);
    }

    @Override
    public Dialog getDialog(
            final ListStore<ReportReference> documentsStore,
            final ProjectDTO project,
            final FlexibleElementDTO flexibleElement,
            final MenuItem menuItem,
            final String phaseName,
            final Authentication authentication,
            final Dispatcher dispatcher,
            final EventBus eventBus) {
        
        if(dialog == null) {
            dialog = createDialog();
        }

        final ReportElementDTO reportElementDTO = (ReportElementDTO) flexibleElement;

        // Clearing the name field
        final TextField<String> nameField = getNameField(dialog);
        nameField.setValue(null);

        // Displays the name of the flexible element
        final LabelField elementNameField = getElementNameField(dialog);
        elementNameField.setValue(flexibleElement.getElementLabel());

        // OK button action
        final Button okButton = dialog.getButtonById(Dialog.OK);
        okButton.removeAllListeners();
        okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final HashMap<String, Serializable> properties = new HashMap<String, Serializable>();
                properties.put("name", nameField.getValue());
                properties.put("reportModelId", reportElementDTO.getModelId());
                properties.put("phaseName", phaseName);
                properties.put("projectId", project.getId());
                properties.put("containerId", project.getId());
                properties.put("flexibleElementId", reportElementDTO.getId());

                final CreateEntity createEntity = new CreateEntity("ProjectReport", properties);
                dispatcher.execute(createEntity, null, new AsyncCallback<CreateResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        dialog.hide();
                        MessageBox.alert(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateError(), null);
                    }

                    @Override
                    public void onSuccess(CreateResult result) {
                        final ReportReference reportReference = new ReportReference();
                        reportReference.setId(result.getNewId());
                        reportReference.setName(nameField.getValue());
                        reportReference.setFlexibleElementLabel(reportElementDTO.getElementLabel());
                        reportReference.setEditorName(authentication.getUserShortName());
                        reportReference.setPhaseName(phaseName);
                        reportReference.setLastEditDate(new Date());

                        documentsStore.add(reportReference);
                        menuItem.setEnabled(false);

                        dialog.hide();

                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateSuccess());

                        // Open the new report
                        final ProjectState targetPage = new ProjectState(project.getId());
                        targetPage.setCurrentSection(ProjectPresenter.REPORT_TAB_INDEX);
                        targetPage.setArgument(Integer.toString(result.getNewId()));

                        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, targetPage));
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public boolean shouldEnableMenuItem(final MenuItem menuItem, final LocalizedElement element, final Dispatcher dispatcher) {
        final GetValue getValue = new GetValue(element.getPhase().getParentProjectDTO().getId(),
                                         element.getElement().getId(),
                                         element.getElement().getEntityName());

        dispatcher.execute(getValue, null, new AsyncCallback<ValueResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Log.error("Could not retrieves the value for element "+element.toString());
            }

            @Override
            public void onSuccess(ValueResult result) {
                if(result == null || !result.isValueDefined())
                    menuItem.setEnabled(true);
                else
                    menuItem.setTitle(I18N.CONSTANTS.reportNoCreate());
            }
            
        });

        return false;
    }
}
