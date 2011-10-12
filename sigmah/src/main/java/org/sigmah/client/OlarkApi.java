package org.sigmah.client;

public class OlarkApi {

	public static native void updateEmailAddress(String email) /*-{
		$wnd.olark('api.visitor.updateEmailAddress', {emailAddress: email});
	}-*/;
	
	public static native void updateFullName(String name) /*-{
		$wnd.olark('api.visitor.updateFullName', {fullName: name});
	}-*/;
	
	
	
}
