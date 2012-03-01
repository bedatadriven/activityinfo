package org.sigmah.client.page.common.columns;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

public class ReadTextColumn extends ColumnConfig {
	public ReadTextColumn(String property, String header, int width) {
		super();
		
		setHeader(header);
		setId(property);
		setWidth(width);
	}
}
