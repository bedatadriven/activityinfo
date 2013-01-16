package org.activityinfo.client.page.entry.sitehistory;

import java.util.Arrays;
import java.util.List;

class Item {
	private String msg;
	private List<ItemDetail> details;

	Item() {
	}

	Item(String msg) {
		this.msg = msg;
	}

	void setMsg(String msg) {
		this.msg = msg;
	}

	void setDetails(List<ItemDetail> details) {
		this.details = details;
	}
	
	boolean hasContent() {
		return msg != null || details != null;
	}
	
	@Override
	public String toString() {
		StringBuilder html = new StringBuilder();
		
		html.append("<p>");

		if (msg != null) {
			html.append("<span style='color: #15428B; font-weight: bold;'>");
			html.append(msg);
			html.append("</span><br/>");
		}
		
		if (details != null && details.size() > 0) {
			html.append("<ul style='margin:0px 0px 10px 20px; font-size: 11px;'>");
			for (ItemDetail detail : details) {
				html.append("<li>");
				html.append(detail);
				html.append("</li>");
			}
			html.append("</ul>");
		}
		
		html.append("</p>");
		
		return html.toString();
	}
	
	void appendTo(StringBuilder html) {
		if (hasContent()) {
			html.append(toString());
		}
	}
	
    public static String appendAll(Item... items) {
    	return appendAll(Arrays.asList(items));
    }

    public static String appendAll(List<Item> items) {
    	StringBuilder html = new StringBuilder();
    	for (Item item : items) {
    		item.appendTo(html);
    	}
    	return html.toString();
    }
}