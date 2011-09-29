package org.sigmah.client.page.entry;

import java.util.List;

import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;

public class SiteTreeLoader extends BaseTreeLoader<SiteDTO> {
	@Override
	protected void onLoadFailure(Object loadConfig, Throwable t) {
		super.onLoadFailure(loadConfig, t);
	}

	@Override
	protected void onLoadSuccess(Object loadConfig, List<SiteDTO> result) {
		super.onLoadSuccess(loadConfig, result);
	}

	public SiteTreeLoader(SiteTreeProxy proxy) {
		super(proxy);
	}
	
	@Override
	public boolean hasChildren(SiteDTO parent) {
		return parent instanceof YearViewModel || parent instanceof MonthViewModel;
	}
}