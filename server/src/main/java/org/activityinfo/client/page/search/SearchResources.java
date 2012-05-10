package org.activityinfo.client.page.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SearchResources extends ClientBundle {
	
	static final SearchResources INSTANCE = GWT.create(SearchResources.class);
	
	@Source("SitesTemplate.html")
	TextResource sitesTemplate();
	
	@Source("EntitiesTemplate.html")
	TextResource entitiesTemplate();
	
	@Source("SearchStyles.css")
	SearchStyles searchStyles();
	
	@Source("location.png")
	ImageResource location();	
	
	@Source("database.png")
	ImageResource database();	
	
	@Source("activity.png")
	ImageResource activity();	
	
	@Source("date.png")
	ImageResource date();	
	
	@Source("addEdited.png")
	ImageResource addEdited();	
	
	@Source("search.png")
	ImageResource search();	
	
	@Source("searchSmall.png")
	ImageResource searchSmall();	  
	
	interface SearchStyles extends CssResource {
		String searchBox();
		String link();
		String panelEntityResults();
		String filterView();
		String searchField();
	}
}
