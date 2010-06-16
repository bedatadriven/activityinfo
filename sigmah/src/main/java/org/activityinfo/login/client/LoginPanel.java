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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class LoginPanel {
    private FormElement form;
    private InputElement emailTextBox;
    private InputElement passwordTextBox;
    private Element statusElement;

    public LoginPanel() {
        form = DOM.getElementById("loginForm").cast();
        emailTextBox = DOM.getElementById("emailTextBox").cast();
        passwordTextBox = DOM.getElementById("passwordTextBox").cast();
        statusElement = DOM.getElementById("loginStatus");
    }

    public FormElement getForm() {
        return form;
    }

    public InputElement getEmailTextBox() {
        return emailTextBox;
    }

    public InputElement getPasswordTextBox() {
        return passwordTextBox;
    }

    public void setBusy(String message) {
        StringBuilder html = new StringBuilder();
        html.append("<img width=\"16\" height=\"16\" src=\"").append(GWT.getModuleBaseURL()).append("working.gif").append("\">");
        html.append(message);
        statusElement.setInnerHTML(html.toString());
    }

    public void setError(String errorMessage) {
        StringBuilder html = new StringBuilder();
        html.append("<span style=\"color: red; font-weight: bold\">").append(errorMessage).append("</span>");
        statusElement.setInnerHTML(html.toString());
    }

    public void clearStatus() {
        statusElement.setInnerHTML("&nbsp;");
    }

}
