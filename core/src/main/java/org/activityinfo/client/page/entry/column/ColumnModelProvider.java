package org.activityinfo.client.page.entry.column;

import org.activityinfo.client.page.entry.grouping.GroupingModel;
import org.activityinfo.shared.command.Filter;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ColumnModelProvider  {

	void get(Filter filter, GroupingModel grouping, AsyncCallback<ColumnModel> callback);
	
}
