package org.sigmah.client.page.common.filter;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.shared.dao.Filter;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class LocationFilterPanel  extends ContentPanel  implements FilterPanel {
	private TreeLoader<ModelData> loader;
	private TreeStore<ModelData> store;
	private TreePanel<ModelData> tree;
	private final Dispatcher service;	
	
	public LocationFilterPanel(Dispatcher service) {
		super();
		
		this.service = service;
	}

	@Override
	public Filter getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setValue(Filter value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		// TODO Auto-generated method stub
		
	}

}
