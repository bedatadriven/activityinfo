package org.activityinfo.chrome.client;

import org.activityinfo.login.shared.LoginService;
import org.activityinfo.login.shared.LoginServiceAsync;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class LoginForm extends Dialog {
	
	private TextField<String> emailField;
	private TextField<String> passwordField;
	
	private AsyncCallback<AuthenticatedUser> callback;

	public LoginForm() {
		FormLayout layout = new FormLayout();
		layout.setLabelPad(20);
		setLayout(layout);
		setWidth(450);
		setHeight(250);
		setStyleAttribute("padding", "10px");
		
		emailField = new TextField<String>();
		emailField.setFieldLabel("Email");
		add(emailField);
		
		passwordField = new TextField<String>();
		passwordField.setPassword(true);
		passwordField.setFieldLabel("Password");
		add(passwordField);
	}

	public void show(AsyncCallback<AuthenticatedUser> callback) {
		this.callback = callback;
		show();
	}
	
	@Override
	protected void onButtonPressed(Button button) {
		LoginServiceAsync service = GWT.create(LoginService.class);
		((ServiceDefTarget) service).setServiceEntryPoint("http://localhost:8888/Login/service");
		service.login(emailField.getValue(), passwordField.getValue(), true, new AsyncCallback<AuthenticatedUser>() {
			
			@Override
			public void onSuccess(AuthenticatedUser result) {
				hide();
				callback.onSuccess(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				MessageBox.alert("Login failed", caught.getMessage(), null);
			}
		});
	}
	

}
