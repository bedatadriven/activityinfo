package org.activityinfo.client.page.entry.form;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;

public class FormSectionModel<M> extends BaseModelData {

	private FormSection<M> section;
	
	public FormSectionModel() {
	}
	
	public static <M> FormSectionModel<M> forComponent(FormSection<M> section) {
		FormSectionModel<M> model = new FormSectionModel<M>();
		model.section = section;
		return model;
	}
	
	public FormSectionModel<M> withHeader(String header) {
		set("header", header);
		return this;
	}
	
	public FormSectionModel<M>  withDescription(String description) {
		set("description", description);
		return this;
	}
	
	public Component getComponent() {
		return section.asComponent();
	}
	
	public FormSection<M> getSection() {
		return section;
	}
	
}
