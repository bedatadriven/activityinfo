package org.activityinfo.client.page.common;

import org.activityinfo.client.page.search.SearchResources;

import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.google.gwt.user.client.Element;

public class SearchField extends TriggerField<String> {

	public SearchField() {
		super();
		
		SearchResources.INSTANCE.searchStyles().ensureInjected();
		
		setTriggerStyle("x-form-search-trigger");
	}

	@Override
	protected void onRender(Element target, int index) {
		super.onRender(target, index);
		setTriggerStyle("x-form-search-trigger");
	}
	
}
