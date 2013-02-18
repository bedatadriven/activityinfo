package org.activityinfo.server.endpoint.refine;

public class QueryUtils {

	static String cleanupQuery(String text) {
		int parentStart = text.indexOf('(');
		if(parentStart != -1) {
			text = text.substring(0, parentStart);
		}
		return text.trim();
	}

}
