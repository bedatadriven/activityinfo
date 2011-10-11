package org.sigmah.client;

public class OlarkApi {

	public static native void updateEmailAddress(String email) /*-{
		olark('api.visitor.updateEmailAddress', {emailAddress: email});
	}-*/;
	
	public static native void updateFullName(String name) /*-{
		olark('api.visitor.updateFullName', {fullName: name});
	}-*/;
	
	
	
}
