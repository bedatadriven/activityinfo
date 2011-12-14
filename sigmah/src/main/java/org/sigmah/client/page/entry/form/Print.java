package org.sigmah.client.page.entry.form;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.UIObject;

public class Print {
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
		try {
			doit(html);
		} catch (Throwable exc) {
			Window.alert(exc.getMessage());
		}
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
