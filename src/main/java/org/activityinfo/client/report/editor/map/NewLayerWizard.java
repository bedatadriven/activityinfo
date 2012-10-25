package org.activityinfo.client.report.editor.map;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Displays a modal window enabling the user to add a layer by selecting one or more indicators
 */
public final class NewLayerWizard implements Wizard {

	private IndicatorPage indicatorPage;
	private LayerTypePage layerTypePage;
	private AdminLevelPage adminLevelPage;

	public NewLayerWizard(Dispatcher dispatcher) {
		indicatorPage = new IndicatorPage(dispatcher);
		indicatorPage.addListener(Events.SelectionChange, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				onIndicatorsChanged();
			}
		});
		layerTypePage = new LayerTypePage();
		layerTypePage.addListener(Events.Change, new Listener<BaseEvent>() {

			@Override
			public void handleEvent(BaseEvent be) {
				onTypeChanged();
			}
		});
		adminLevelPage = new AdminLevelPage(dispatcher);
	}
	
	private void onIndicatorsChanged() {
		adminLevelPage.setIndicators(indicatorPage.getSelectedIds());
	}

	private void onTypeChanged() {
		adminLevelPage.setEnabled(layerTypePage.newLayer() instanceof PolygonMapLayer);
	}

	public MapLayer createLayer() {
		MapLayer layer = layerTypePage.newLayer();
		layer.setName(composeName());
		for(Integer indicatorId : indicatorPage.getSelectedIds()) {
			layer.addIndicatorId(indicatorId);
		}
		if(layer instanceof PolygonMapLayer) {
			((PolygonMapLayer) layer).setAdminLevelId(adminLevelPage.getSelectedLevelId());
		}
		return layer;
	}

	private String composeName() {
		return indicatorPage.getSelection().iterator().next().getName();
	}
	
	@Override
	public WizardPage[] getPages() {
		return new WizardPage[] { indicatorPage, layerTypePage, adminLevelPage };
	}

	@Override
	public String getTitle() {
		return "New Layer";
	}

	@Override
	public boolean isPageEnabled(WizardPage page) {
		if(page == adminLevelPage) {
			return layerTypePage.newLayer() instanceof PolygonMapLayer;
		} else {
			return true;			
		}
	}


}
