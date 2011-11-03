package org.sigmah.client.page.entry.place;

import java.util.Arrays;
import java.util.List;

import org.sigmah.client.page.PageId;
import org.sigmah.client.page.common.grid.AbstractPagingGridPageState;
import org.sigmah.client.page.entry.DataEntryPage;
import org.sigmah.shared.command.Filter;

public class DataEntryPlace extends AbstractPagingGridPageState {

	public DataEntryPlace() {
		super();
	}

	@Override
	public List<PageId> getEnclosingFrames() {
		return Arrays.asList(DataEntryPage.PAGE_ID);
	}

	@Override
	public String serializeAsHistoryToken() {
		return "";
	}

	@Override
	public PageId getPageId() {
		return DataEntryPage.PAGE_ID;
	}

	public Filter getFilter() {
		return new Filter();
	}
	
}