package org.sigmah.client.page.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SearchResources extends ClientBundle {
	
	public static final SearchResources INSTANCE = GWT.create(SearchResources.class);
	
	@Source("SitesTemplate.html")
	public TextResource sitesTemplate();
	
	@Source("SearchStyles.css")
	public SearchStyles searchStyles();
	
	@Source("location.png")
	public ImageResource location();	
	
	@Source("database.png")
	public ImageResource database();	
	
	@Source("activity.png")
	public ImageResource activity();	
	
	@Source("date.png")
	public ImageResource date();	
	
	public interface SearchStyles extends CssResource {
		String searchBox();
		String link();
	}
}
