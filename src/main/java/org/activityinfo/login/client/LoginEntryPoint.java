package org.activityinfo.login.client;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.login.shared.LoginException;
import org.activityinfo.login.shared.LoginService;
import org.activityinfo.login.shared.LoginServiceAsync;
import org.activityinfo.login.shared.PasswordExpiredException;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Self-standing login component that can be embedded in an IFRAME.
 * The actual form is used from the host page.
 * 
 * <p>
 * See http://borglin.net/gwt-project/?page_id=467 for rationale.
 */
public class LoginEntryPoint implements EntryPoint {
	
	private static final String USERNAME_ID = "emailField";
	private static final String PASSWORD_ID = "passwordField";
	
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	
	@Override
	public void onModuleLoad() {
		injectLoginFunction(this);
	}

	private native void injectLoginFunction(LoginEntryPoint view) /*-{
		$wnd.__do_login = function(){
	    	view.@org.activityinfo.login.client.LoginEntryPoint::doLogin()();
		}
	}-*/;
	
	private void doLogin() {
		String email = ((InputElement) Document.get().getElementById(USERNAME_ID)).getValue();
		String password = ((InputElement) Document.get().getElementById(PASSWORD_ID)).getValue();

		loginService.login(email, password, false, new AsyncCallback<AuthenticatedUser>() {
			
			@Override
			public void onSuccess(AuthenticatedUser result) {
				Window.open("/", "_top", null);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof PasswordExpiredException) {
					Window.alert("For security reasons, your password has expired. You will receive an email with instructions on " +
							"how to reset your password.");
				} else if (caught instanceof LoginException) {
					Window.alert("Invalid email or password.");
				} else {
					Window.alert("Error connecting to server");
				}
			}
		});		
	}
}
