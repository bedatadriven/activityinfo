

package org.activityinfo.client.page.common.dialog;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/*
 * Default implementation for a FormDialog that displays a FormPanel
 */
public class FormDialogImpl<FormT extends FormPanel> extends Window implements AsyncMonitor, FormDialogTether {
    protected FormT form;
    private Status status;
    protected Button saveButton;
    protected Button cancelButton;
    private String workingText = I18N.CONSTANTS.saving();
    private FormDialogCallback callback = null;
    private boolean asyncCallInProgress = false;
    private boolean asyncCallCancelled;


    public FormDialogImpl(final FormT form) {
        this.form = form;
    	
        initializeComponent();

        adjustAppearanceToFitDialog(form);

        add(form);
        
        createStatusButtonBar();
        createSaveButton();
        createCancelButton();
    }

	private void adjustAppearanceToFitDialog(final FormT form) {
		if (!form.isRendered()) {
            form.setHeaderVisible(false);
            form.setFrame(false);
        }
	}

	private void createCancelButton() {
        cancelButton = new Button(I18N.CONSTANTS.cancel());
        cancelButton.setIcon(IconImageBundle.ICONS.cancel());
        addButton(cancelButton);
        cancelButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
                hide();
            }
        });
	}

	private void createSaveButton() {
		saveButton = new Button(I18N.CONSTANTS.save());
        saveButton.setIcon(IconImageBundle.ICONS.save());
        saveButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
                // use a deferred handler to make sure we get any change
                // events first
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!form.isValid()) {
                            MessageBox.alert(getHeading(),
                                    I18N.CONSTANTS.pleaseCompleteForm(), null);
                        } else {
                            onValidated();
                        }
                    }
                });
            }
        });
        addButton(saveButton);
	}

	private void createStatusButtonBar() {
		status = new Status();
        status.setWidth(200);
        this.getButtonBar().add(status);
	}

	private void initializeComponent() {
		setModal(true);
        setLayout(new FitLayout());
        setModal(true);
        setClosable(false);
        setBodyStyle("padding: 5px;");
        setLayout(new FitLayout());
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
        status.setBusy(I18N.CONSTANTS.connectionProblem());
    }

    @Override
    public boolean onRetrying() {
        if (asyncCallCancelled) {
            return false;
        }

        cancelButton.disable();
        status.setBusy(I18N.CONSTANTS.retrying());

        return true;
    }

    @Override
    public void onServerError() {
        form.enable();
        saveButton.enable();
        cancelButton.enable();
        status.clearStatus("");
        MessageBox.alert(this.getHeading(), I18N.CONSTANTS.serverError(), null);
    }

    @Override
    public void onCompleted() {
        status.clearStatus("");
        form.enable();
        saveButton.enable();
        cancelButton.enable();
    }
}
