package org.sigmah.client.page.entry.column;

import org.sigmah.client.page.entry.grouping.GroupingModel;
import org.sigmah.shared.command.Filter;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ColumnModelProvider  {

	void get(Filter filter, GroupingModel grouping, AsyncCallback<ColumnModel> callback);
	
}
