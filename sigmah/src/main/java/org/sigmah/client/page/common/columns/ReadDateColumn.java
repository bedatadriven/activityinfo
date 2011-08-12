package org.sigmah.client.page.common.columns;

import com.google.gwt.i18n.client.DateTimeFormat;

public class ReadDateColumn extends ReadTextColumn {

	public ReadDateColumn(String property, String header, int width) {
		super(property, header, width);
		
	    setId(property);
	    setHeader(header);
	    setWidth(width);
	    setRowHeader(true);
	    setDateTimeFormat(DateTimeFormat.getFormat("yyyy-MMM-dd"));
	}
}