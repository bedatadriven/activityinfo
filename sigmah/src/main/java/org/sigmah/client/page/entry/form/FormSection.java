package org.sigmah.client.page.entry.form;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class FormSection extends LayoutContainer {

	private final FormLayout layout;

	public FormSection() {
		layout = new FormLayout();
		setLayout(layout);
		setScrollMode(Scroll.AUTOY);
		setStyleAttribute("padding", "10px");
	}
	
	protected final FormLayout getFormLayout() {
		return layout;
	}
	
}
