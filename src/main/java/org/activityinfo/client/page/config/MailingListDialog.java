package org.activityinfo.client.page.config;

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

import java.util.List;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.dto.UserPermissionDTO;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MailingListDialog extends Window {

    private final Dispatcher service;
    private final int dbId;

    private Button closeButton;
    private TextArea emailsTextField;

    public MailingListDialog(Dispatcher service, int dbId) {

        this.service = service;
        this.dbId = dbId;

        initializeComponent();
        hide();

        createTextField();
        createCloseButton();
        loadUsers();
    }

    private void initializeComponent() {
        setHeading(I18N.CONSTANTS.mailingList());
        setModal(true);
        setLayout(new FitLayout());
        setModal(true);
        setClosable(false);
        setBodyStyle("padding: 5px;");
        setLayout(new FitLayout());
    }

    private void createCloseButton() {
        closeButton = new Button(I18N.CONSTANTS.close());
        addButton(closeButton);
        closeButton.addListener(Events.Select, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                hide();
            }
        });
    }

    private void createTextField() {
        emailsTextField = new TextArea();
        emailsTextField.setHeight("175px");
        emailsTextField.setWidth("250px");
        emailsTextField.selectAll();
        add(emailsTextField);
    }

    @Override
    public void show() {
        super.show();
    }

    private void loadUsers() {
        service.execute(new GetUsers(dbId), new AsyncCallback<UserResult>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(UserResult result) {
                String emails = createMailingList(result.getData());
                emailsTextField.setValue(emails);
                show();
            }
        });
    }

    private String createMailingList(List<UserPermissionDTO> users) {
        StringBuilder emails = new StringBuilder();

        for (UserPermissionDTO dto : users) {
            emails.append("\"" + dto.getName() + "\"");
            emails.append(" ");
            emails.append("<" + dto.getEmail() + ">,");
            emails.append(" ");
        }

        return emails.toString();

    }

}
