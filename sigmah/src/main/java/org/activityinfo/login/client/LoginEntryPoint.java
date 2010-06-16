/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;

public class LoginEntryPoint implements EntryPoint {
    private LoginPanel panel;
    private LoginMessages messages;
    protected FormPanel form;

    @Override
    public void onModuleLoad() {
        panel = new LoginPanel();
        messages = GWT.create(LoginMessages.class);
        form = FormPanel.wrap(panel.getForm(), true);

        bindForm();
    }

    private void bindForm() {
        form.addSubmitHandler(new FormPanel.SubmitHandler() {
            @Override
            public void onSubmit(FormPanel.SubmitEvent event) {
                if(! validateEmail() || !validatePassword()) {
                    event.cancel();
                } else {
                    panel.setBusy(messages.connecting());
                }
            }
        });
        form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                String result = event.getResults();
                if(result.equals("OK")) {
                    onLoginSuccessful();
                } else if(result.equals("BAD LOGIN")) {
                    panel.setError(messages.invalidLogin());
                } else {
                    panel.setError(messages.invocationException());
                }
            }
        });
    }

    private boolean validatePassword() {
        if(isBlank(panel.getPasswordTextBox().getValue())) {
            Window.alert(messages.passwordMissing());
            panel.getPasswordTextBox().focus();
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = panel.getEmailTextBox().getValue();

        if(isBlank(email)) {
            Window.alert(messages.emailAddressMissing());
            panel.getEmailTextBox().focus();
            return false;
        }
        if(email.indexOf("@") == -1) {
            Window.alert(messages.emailAddressInvalid());
            panel.getEmailTextBox().focus();

            return false;
        }
        return true;
    }

    private void onLoginSuccessful() {
        panel.setBusy(messages.loginSuccessful());
        Window.Location.assign("http://www.activityinfo.org");
    }

    private boolean isBlank(String email) {
        return email == null || email.length() == 0;
    }
}
