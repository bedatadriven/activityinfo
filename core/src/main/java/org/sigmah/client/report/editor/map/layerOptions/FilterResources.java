package org.sigmah.client.report.editor.map.layerOptions;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface FilterResources extends ClientBundle {

	static final FilterResources INSTANCE = GWT.create(FilterResources.class);
	static final FilterMessages MESSAGES = GWT.create(FilterMessages.class);
	
	
	@Source("Filter.css")
	@NotStrict
	Style style();
	
	@Source("Filter.png")
	ImageResource filterIcon();
		
	@Source("CrossCircle.png")
	ImageResource crossIcon();
	
	interface Style extends CssResource {
		
	}
	
	interface FilterMessages extends Messages {
		
		@DefaultMessage("Before {0,date,medium}")
		String beforeDate(Date date);
		
		@DefaultMessage("After {0,date,medium}")
		String afterDate(Date date);
		
		@DefaultMessage("{0,date,medium} - {1,date,medium}")
		String betweenDates(Date date1, Date date2);
		
		@DefaultMessage("{0,list}")
		String filteredPartnerList(List<String> names);
		
	}
}
