package org.sigmah.client.page.map;

import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.map.BaseMap;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.user.client.rpc.AsyncCallback;

/*
 * Displays a list of options and hints for the map
 */
public class MapOptionsWidget extends ContentPanel {
	private FieldSet fieldsetBaseMaps = new FieldSet();
	private RadioGroup radiogroupBaseMap = new RadioGroup();
	private Dispatcher service;
	private List<BaseMap> baseMaps;
	
	public MapOptionsWidget(Dispatcher service) {
		this.service=service;
		
		getBaseMaps();
		
		createBaseMapOptions(baseMaps);
		createFixedZoomHint();
		createFixedPanningHint();
	}


	private void getBaseMaps() {
		GetBaseMaps getBaseMaps = new GetBaseMaps();
		
		service.execute(getBaseMaps, null, new AsyncCallback<BaseMapResult>() {
			@Override
			public void onFailure(Throwable caught) {
				failLoadingBaseMaps();
			}

			@Override
			public void onSuccess(BaseMapResult result) {
				if (result.getBaseMaps() == null) {
					failLoadingBaseMapsEmpty();
				} else {
					baseMaps = result.getBaseMaps();
				}
			}
		});
	}
	
	private void failLoadingBaseMapsEmpty() {
		Label labelFailLoading = new Label(I18N.CONSTANTS.failBaseMapLoadingCount());
		add(labelFailLoading);
	}

	private void failLoadingBaseMaps() {
		Label labelFailLoading = new Label(I18N.CONSTANTS.failBaseMapLoading());
		add(labelFailLoading);
	}

	private void createFixedPanningHint() {
		// TODO Auto-generated method stub
		
	}

	private void createFixedZoomHint() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Adds a radiobutton for every found basemap
	 */
	private void createBaseMapOptions(List<BaseMap> basemaps) {
		if (basemaps != null) {
			for (BaseMap baseMap : basemaps) {
				Radio radioBaseMap = new Radio();
				radioBaseMap.setTitle(baseMap.getName());
				radiogroupBaseMap.add(radioBaseMap);
			}
			
			radiogroupBaseMap.addListener(Events.Change, new Listener<FieldEvent>() {
				@Override
				public void handleEvent(FieldEvent be) {
					// Update the map to display the correct basemap
			}});
		}
	}
}
