/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.dialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import org.sigmah.client.Application;
import org.sigmah.client.dispatch.AsyncMonitor;

public class FormDialogImpl<FormT extends FormPanel> extends Window implements AsyncMonitor, FormDialogTether {

    protected FormT form;

    private Status status;

    protected final Button saveButton;
    protected final Button cancelButton;

    private String workingText = Application.CONSTANTS.saving();

    private FormDialogCallback callback = null;

    private boolean asyncCallInProgress = false;
    private boolean asyncCallCancelled;


    public FormDialogImpl(final FormT form) {

        /*
           * Configure this window
           */
        setModal(true);
        setLayout(new FitLayout());
        setModal(true);
        //setCloseAction(CloseAction.HIDE);
        setClosable(false);
        setBodyStyle("padding: 5px;");
        setLayout(new FitLayout());

        /*
           * Adjust the form panel's appearance
           * to fit in nicely with the dialog
           */
        this.form = form;
        if (!form.isRendered()) {
            form.setHeaderVisible(false);
            form.setFrame(false);
        }

        add(form);

        /*
           * Create the status button bar
           */

        status = new Status();
        status.setWidth(200);
        this.getButtonBar().add(status);
        //	this.getButtonBar().add(new FillToolItem());

        saveButton = new Button(Application.CONSTANTS.save());
        saveButton.setIcon(Application.ICONS.save());
        saveButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
                // use a deferred handler to make sure we get any change
                // events first
                DeferredCommand.addCommand(new Command() {
                    @Override
                    public void execute() {
                        if (!form.isValid()) {
                            MessageBox.alert(getHeading(),
                                    Application.CONSTANTS.pleaseCompleteForm(), null);
                        } else {
                            onValidated();
                        }
                    }
                });
            }
        });
        addButton(saveButton);

        cancelButton = new Button(Application.CONSTANTS.cancel());
        cancelButton.setIcon(Application.ICONS.cancel());
        addButton(cancelButton);

        cancelButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
//				if(getCloseAction() == CloseAction.CLOSE) {
//					close();
//				} else {
                hide();
//				}
            }
        });
    }


    public FormT getForm() {
        return form;
    }

    public Button getSaveButton() {
        return saveButton;
    }


    public void show(FormDialogCallback callback) {
        this.callback = callback;

        show();
    }


    public void onValidated() {
        if (callback != null) {
            callback.onValidated(this);
        }
    }

    public void onCancel() {
        if (asyncCallInProgress) {
            asyncCallCancelled = true;
        } else {
            hide();
        }
    }

    public String getWorkingText() {
        return workingText;
    }


    public void setWorkingText(String workingText) {
        this.workingText = workingText;
    }

    public Status getStatus() {
        return status;
    }


    @Override
    public void beforeRequest() {

        form.disable();
        saveButton.disable();
        cancelButton.disable();

        asyncCallCancelled = false;

        status.setBusy(workingText);
    }


    @Override
    public void onConnectionProblem() {
        status.setBusy(Application.CONSTANTS.connectionProblem());
    }


    @Override
    public boolean onRetrying() {
        if (asyncCallCancelled) {
            return false;
        }

        cancelButton.disable();
        status.setBusy(Application.CONSTANTS.retrying());

        return true;
    }


    @Override
    public void onServerError() {

        form.enable();
        saveButton.enable();
        cancelButton.enable();
        status.clearStatus("");

        MessageBox.alert(this.getHeading(), Application.CONSTANTS.serverError(), null);
    }


    @Override
    public void onCompleted() {
        status.clearStatus("");

        form.enable();
        saveButton.enable();
        cancelButton.enable();

    }
}
