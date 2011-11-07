package org.sigmah.client.page.entry.form;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class FormSection extends LayoutContainer {

	public FormSection() {
		FormLayout layout = new FormLayout();
		setLayout(layout);
		setScrollMode(Scroll.AUTOY);
		setStyleAttribute("padding", "10px");
	}
	
}
