package org.sigmah.client.page.search;

import com.google.gwt.inject.client.AbstractGinModule;

public class SearchModule  extends AbstractGinModule{

	@Override
	protected void configure() {
        bind(SearchView.class).to(SearchResultsPage.class);
	}

}
