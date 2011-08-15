package org.sigmah.client.page.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SearchResources extends ClientBundle {
	
	public static final SearchResources INSTANCE = GWT.create(SearchResources.class);
	
	@Source("SearchResultTemplate.html")
	public TextResource resultsTemplate();
	
	@Source("LatestAdditionsTemplate.html")
	public TextResource latestAdditionsTemplate();
	
	@Source("SearchStyles.css")
	public SearchStyles searchStyles();
	
	@Source("activity.png")
	public ImageResource activity();
	
	@Source("attribute.png")
	public ImageResource attribute();
	
	@Source("database.png")
	public ImageResource database();
	
	@Source("attributeGroup.png")
	public ImageResource attributeGroup();
	
	@Source("project.png")
	public ImageResource project();
	
	@Source("toreplace.png")
	public ImageResource adminEntity();
	
	@Source("toreplace.png")
	public ImageResource site();
	
	@Source("toreplace.png")
	public ImageResource partner();
	
	public interface SearchStyles extends CssResource {
		String searchBox();
		String link();
	}
}
