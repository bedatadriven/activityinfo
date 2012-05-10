package org.activityinfo.client.page.entry.form;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public abstract class FormSectionWithFormLayout<M> extends LayoutContainer implements FormSection<M> {

	private final FormLayout layout;

	public FormSectionWithFormLayout() {
		layout = new FormLayout();
		setLayout(layout);
		setScrollMode(Scroll.AUTOY);
		setStyleAttribute("padding", "10px");
	}
	
	protected final FormLayout getFormLayout() {
		return layout;
	}

	@Override
	public Component asComponent() {
		return this;
	}
	
	
}
