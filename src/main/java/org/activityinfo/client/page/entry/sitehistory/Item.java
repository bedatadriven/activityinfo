package org.activityinfo.client.page.entry.sitehistory;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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