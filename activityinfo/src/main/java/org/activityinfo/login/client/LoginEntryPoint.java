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
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class LoginEntryPoint implements EntryPoint {
    private LoginServiceAsync service;
    private LoginPanel panel;
    private LoginMessages messages;

    @Override
    public void onModuleLoad() {
        panel = new LoginPanel();
        messages = GWT.create(LoginMessages.class);
        service = (LoginServiceAsync) GWT.create(LoginService.class);
	    ((ServiceDefTarget) service).setServiceEntryPoint( GWT.getModuleBaseURL() + "service");

        Element loginButton = DOM.getElementById("loginButton");
        DOM.sinkEvents(loginButton, Event.ONCLICK);
        DOM.setEventListener(loginButton, new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                if(event.getTypeInt() == Event.ONCLICK) {
                    onLoginButtonClicked();
                }
            }
        });
    }

    private void onLoginButtonClicked() {
        String email = panel.getEmailTextBox().getValue();
        String password = panel.getPasswordTextBox().getValue();

        if (validateEmail(email) && validatePassword(password)) {
            doLogin(email, password);
        }
    }

    private void doLogin(String email, String password) {
        panel.setBusy(messages.connecting());
        service.login(email, password, new AsyncCallback<LoginResult>() {
            @Override
            public void onFailure(Throwable caught) {
                onLoginError(caught);
            }

            @Override
            public void onSuccess(LoginResult result) {
                onLoginSuccessful(result);
            }
        });
    }

    private void onLoginSuccessful(LoginResult result) {
        panel.setBusy(messages.loginSuccessful());
        Window.Location.assign(result.getAppUrl());
    }

    private boolean validatePassword(String password) {
        if(isBlank(password)) {
            Window.alert(messages.passwordMissing());
            panel.getPasswordTextBox().focus();
            return false;
        }
        return true;
    }

    private boolean validateEmail(String email) {
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

    private void onLoginError(Throwable caught) {
        panel.clearStatus();
        if(caught instanceof StatusCodeException) {
            panel.setError(messages.statusCodeException());
        } else if(caught instanceof InvocationException) {
            panel.setError(messages.invocationException());
        } else if(caught instanceof LoginException) {
            panel.setError(messages.invalidLogin());
        }
    }

    private boolean isBlank(String email) {
        return email == null || email.length() == 0;
    }
}
