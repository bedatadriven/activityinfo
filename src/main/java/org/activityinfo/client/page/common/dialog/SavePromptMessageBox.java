

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
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SavePromptMessageBox extends Window implements AsyncMonitor {

    private Status status;
    private SaveChangesCallback callback;
    private Button saveButton;
    private Button discardButton;
    private Button cancelButton;
    private boolean asyncCallCancelled;

    public enum Choice {
        SAVE,
        DISCARD,
        CANCEL
    }

    ;


    public SavePromptMessageBox() {
        /*
           * Configure this window
           */
        setModal(true);
        setClosable(false);
        setBodyStyle("padding: 5px;");
        setWidth(450);
        setHeight(200);
        setHeading(I18N.CONSTANTS.save());
        setLayout(new CenterLayout());
        add(new Html(I18N.CONSTANTS.promptSave()));


        /*
           * Create the status button bar
           */

        status = new Status();
        this.getButtonBar().add(status);

        saveButton = new Button(I18N.CONSTANTS.save());
        saveButton.setIcon(IconImageBundle.ICONS.save());
        saveButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
                callback.save(SavePromptMessageBox.this);
            }
        });
        addButton(saveButton);

        discardButton = new Button(I18N.CONSTANTS.discardChanges());
        discardButton.setIcon(IconImageBundle.ICONS.cancel());
        discardButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                hide();
                callback.discard();
            }
        });
        addButton(discardButton);


        cancelButton = new Button(I18N.CONSTANTS.cancel());
        addButton(cancelButton);

        cancelButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            public void handleEvent(ButtonEvent be) {
                hide();
                callback.cancel();
            }
        });
    }

    public void show(SaveChangesCallback callback) {
        this.callback = callback;

        show();
    }

    @Override
    public void beforeRequest() {

        saveButton.disable();
        discardButton.disable();

        asyncCallCancelled = false;

        status.setBusy(I18N.CONSTANTS.saving());
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

        saveButton.enable();
        cancelButton.enable();
        status.clearStatus("");

        MessageBox.alert(this.getHeading(), I18N.CONSTANTS.serverError(), null);
    }


    @Override
    public void onCompleted() {
        hide();
    }
}

