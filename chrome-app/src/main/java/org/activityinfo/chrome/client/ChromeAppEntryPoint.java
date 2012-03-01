package org.activityinfo.chrome.client;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ChromeAppEntryPoint implements EntryPoint {


	@Override
	public void onModuleLoad() {
		
		Storage storage = Storage.getLocalStorageIfSupported();
		if(storage.getItem(AuthenticatedUser.AUTH_TOKEN_COOKIE) == null) {
			doInitialLogin();
		} else {
			startApp();
		}

	}

	private void doInitialLogin() {
		LoginForm form = new LoginForm();
		form.show(new AsyncCallback<AuthenticatedUser>() {
			
			@Override
			public void onSuccess(AuthenticatedUser result) {
				saveCredentials(result);
			}
	

			@Override
			public void onFailure(Throwable caught) {
				
			}
		});		
	}
	
	
	private void saveCredentials(AuthenticatedUser result) {
		Storage storage = Storage.getLocalStorageIfSupported();
		storage.setItem(AuthenticatedUser.AUTH_TOKEN_COOKIE, result.getAuthToken());
		storage.setItem(AuthenticatedUser.EMAIL_COOKIE, result.getEmail());
		storage.setItem(AuthenticatedUser.USER_ID_COOKIE, Integer.toString(result.getId()));
	}
	
	private void startApp() {
		
	}

}
