package org.sigmah.client.page.entry;

import org.sigmah.client.EventBus;
import org.sigmah.client.page.Frames;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.FilterPanelSet;
import org.sigmah.client.page.common.filter.PartnerFilterPanel;
import org.sigmah.client.page.common.nav.NavigationPanel;
import org.sigmah.client.page.common.widget.VSplitFilteredFrameSet;

import com.google.inject.Inject;

public class DataEntryFrameSet extends VSplitFilteredFrameSet {

	private FilterPanelSet filterPanelSet;
	
	@Inject
	public DataEntryFrameSet(EventBus eventBus, DataEntryNavigator navigator, AdminFilterPanel adminPanel, DateRangePanel datePanel, 
			PartnerFilterPanel partnerPanel) {
        super(Frames.DataEntryFrameSet, new NavigationPanel(eventBus, navigator));
        addFilterPanel(adminPanel);
        addFilterPanel(datePanel);
        addFilterPanel(partnerPanel);
 
        filterPanelSet = new FilterPanelSet(adminPanel, datePanel, partnerPanel);
	}
	
	public FilterPanelSet getFilterPanelSet() {
		return filterPanelSet;
	}
}
