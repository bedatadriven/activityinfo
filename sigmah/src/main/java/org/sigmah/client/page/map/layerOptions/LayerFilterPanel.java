package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.FilterPanelSet;

import com.extjs.gxt.ui.client.widget.ContentPanel;
 
public class LayerFilterPanel extends ContentPanel {
		
	private FilterPanelSet filterPanelSet;
	
	public LayerFilterPanel(Dispatcher dispatcher) {
		FilterResources.INSTANCE.style().ensureInjected();
		
		initializeComponent();
		
		
		DateFilterWidget dateWidget = new DateFilterWidget();
		PartnerFilterWidget partnerFilterWidget = new PartnerFilterWidget(dispatcher);
		
		add(dateWidget);
		add(partnerFilterWidget);
		
		filterPanelSet = new FilterPanelSet(dateWidget, partnerFilterWidget);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filter());
		setIcon(IconImageBundle.ICONS.filter());
	}
	
	public FilterPanelSet getFilterPanelSet() {
		return filterPanelSet;
	}

}
