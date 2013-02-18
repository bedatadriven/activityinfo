package org.activityinfo.client.authentication;

import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Provider;

public class MozAppAuthProvider implements Provider<AuthenticatedUser> {

	@Override
	public AuthenticatedUser get() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private native String getReceipt() /*-{
		
	
	}-*/;

}
