package org.sigmah.client.page.entry;

import org.sigmah.shared.dto.SiteDTO;

import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.Component;

interface SiteGridPanelView {

	void addSelectionChangeListener(SelectionChangedListener<SiteDTO> listener);
	
	void refresh();
	
	Component asComponent();

	SiteDTO getSelection();
}
