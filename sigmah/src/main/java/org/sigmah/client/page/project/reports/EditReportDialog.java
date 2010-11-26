/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.reports;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.Serializable;
import java.util.Map;
import org.sigmah.client.EventBus;
import org.sigmah.client.SigmahInjector;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.result.CreateResult;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class EditReportDialog {
    private static Dialog editReportDialog;

    private static Dialog getDialog() {
        if(editReportDialog == null) {
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

            // Cancel button
            dialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    dialog.hide();
                }
            });

            editReportDialog = dialog;
        }
        return editReportDialog;
    }

    /**
     * Dialog used to <b>create</b> a report from outside the "Report & Documents" page.
     * @param projectModelId Id of the report model to use (should not be null).
     * @param phaseName Name of the phase the report is being created in (can be null).
     * @return The create report dialog.
     */
    public static Dialog getDialog(final Map<String, Serializable> properties,
            final ProjectState targetPage, final EventBus eventBus, final Dispatcher dispatcher) {
        final Dialog dialog = getDialog();

        // OK Button
        final Button okButton = dialog.getButtonById(Dialog.OK);

        okButton.removeAllListeners();
        okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                final String name = ((TextField<String>) dialog.getWidget(0)).getValue();

                properties.put("name", name);

                final CreateEntity createEntity = new CreateEntity("ProjectReport", properties);
                dispatcher.execute(createEntity, null, new AsyncCallback<CreateResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateError(), null);
                    }

                    @Override
                    public void onSuccess(CreateResult result) {
                        Notification.show(I18N.CONSTANTS.projectTabReports(), I18N.CONSTANTS.reportCreateSuccess());
                        targetPage.setArgument(Integer.toString(result.getNewId()));

                        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, targetPage));
                    }
                });

                dialog.hide();
            }

        });

        return dialog;
    }

    
}
