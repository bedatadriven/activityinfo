package org.sigmah.client.page.common;

import org.sigmah.client.page.search.SearchResources;

import com.extjs.gxt.ui.client.widget.form.TriggerField;

public class SearchField extends TriggerField<String> {

	public SearchField() {
		super();
		
		SearchResources.INSTANCE.searchStyles().ensureInjected();
		
		setTriggerStyle("searchField");
	}
	
}
