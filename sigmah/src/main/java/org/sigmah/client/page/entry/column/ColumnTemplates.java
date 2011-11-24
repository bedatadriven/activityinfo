package org.sigmah.client.page.entry.column;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface ColumnTemplates extends SafeHtmlTemplates {

	public static final ColumnTemplates INSTANCE = GWT.create(ColumnTemplates.class);
	
	@Template("<span>{0}<br><i>{1}</i></span>")
	SafeHtml locationCell(String location, String axe);
	
	@Template("<span qtip=\"{0}\">{1}</span>")
	SafeHtml adminCell(String quickTip, SafeHtml summary);

	
	
}
