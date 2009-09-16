package org.activityinfo.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class Downloader {

	public static String CSV = "csv";
	
	public static void downloadSites(int activityId, String format) throws Exception {
		
		StringBuilder url = new StringBuilder();
		
		url.append(GWT.getModuleBaseURL() + "download?");
		url.append("session=");
		url.append(Window.Location.getParameter("session"));
		url.append("&format=");
		url.append(format);
		url.append("&activity=");
		url.append(activityId);
	//	url.append(config.toQueryString());
		
		Window.open(url.toString(), "_top", "");
	}
	
}
