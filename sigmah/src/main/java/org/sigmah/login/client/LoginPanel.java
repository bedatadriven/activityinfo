/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.login.client;

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
