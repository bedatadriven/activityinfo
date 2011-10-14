package org.sigmah.client.page.entry;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelData;

public class SiteTreeLoader extends BaseTreeLoader<ModelData> {
	private TimeTreeProxy timeTreeProxy;
	@Override
	protected void onLoadFailure(Object loadConfig, Throwable t) {
		super.onLoadFailure(loadConfig, t);
	}

	@Override
	protected void onLoadSuccess(Object loadConfig, List<ModelData> result) {
		super.onLoadSuccess(loadConfig, result);
	}

	public SiteTreeLoader(TimeTreeProxy proxy) {
		super(proxy);
		this.timeTreeProxy=proxy;
	}
	
	@Override
	public boolean hasChildren(ModelData parent) {
		return timeTreeProxy.hasChildren(parent);
	}
}