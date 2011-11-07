package org.sigmah.client.page.entry.form;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Component;

public class FormSectionModel extends BaseModelData {

	private Component component;
	
	public FormSectionModel() {
	}
	
	public FormSectionModel forComponent(Component component) {
		this.component = component;
		return this;
	}
	
	public FormSectionModel withHeader(String header) {
		set("header", header);
		return this;
	}
	
	public FormSectionModel withDescription(String description) {
		set("description", description);
		return this;
	}
	
	public Component getComponent() {
		return component;
	}
}
