package org.activityinfo.client.page.entry.form;

import com.extjs.gxt.ui.client.widget.Component;

public interface FormSection<Model> {

	boolean validate();
	
	void updateModel(Model m);
	
	void updateForm(Model m);
	
	Component asComponent();
}
