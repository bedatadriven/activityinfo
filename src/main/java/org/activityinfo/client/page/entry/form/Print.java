package org.activityinfo.client.page.entry.form;

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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;

public class Print {
	
	private Print(){
		
	}
	
	public static native void it()
	/*-{
		$wnd.print();
	}-*/;

	public static native void doit(String html)
	/*-{
		var frame = $doc.getElementById('__printingFrame');

		if (!frame) {
			$wnd.alert("Error: Can't find printing frame.");
			return;
		}

		frame = frame.contentWindow;
		var doc = frame.document;
		doc.open();
		doc.write(html);
		doc.close();
		for ( var cii = 0; cii < 100000; cii++) {
		}
		frame =

		$doc.getElementById('__printingFrame');
		frame = frame.contentWindow;
		frame.focus();
		frame.print();
	}-*/;

	public static void it(String html) {
			doit(html);
	}

	public static void it(UIObject obj) {
		it("", obj.getElement().toString());
	}

	public static void it(Element element) {
		it("", element.toString());
	}

	public static void it(String style, String it) {
		it("<html><head>" + style + "</head>\n<body>" + it + "</body></html>");
	}

	public static void it(String style, UIObject obj) {
		it(style, obj.getElement().toString());
	}

	public static void it(String style, Element element) {
		it(style, element.toString());
	}
} // end of class
