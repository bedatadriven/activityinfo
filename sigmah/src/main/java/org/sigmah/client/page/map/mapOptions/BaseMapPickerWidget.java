package org.sigmah.client.page.map.mapOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetBaseMaps;
import org.sigmah.shared.command.result.BaseMapResult;
import org.sigmah.shared.map.BaseMap;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;

/*
 * Displays a list of options and hints for the map
 */
public class BaseMapPickerWidget extends LayoutContainer implements HasValue<BaseMap>{
	private RadioGroup radiogroupBaseMap = new RadioGroup();
	private Dispatcher service;
	private List<BaseMap> baseMaps;
	private BaseMap selectedBaseMap = null;
	private Map<Radio, BaseMap> baseMapPerRadio = new HashMap<Radio, BaseMap>();
	private VerticalPanel panelBaseMaps = new VerticalPanel(); 
	
	public BaseMapPickerWidget(Dispatcher service) {
		this.service=service;
		
		initializeComponent();
		
		getBaseMaps();

		createFixedZoomHint();
		createFixedPanningHint();
	}


	private void initializeComponent() {
		panelBaseMaps.setAutoWidth(true);
		add(panelBaseMaps);
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
					createBaseMapOptions();
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
	private void createBaseMapOptions() {
		if (baseMaps != null) {
			for (BaseMap baseMap : baseMaps) {
				Radio radioBaseMap = new Radio();
				radioBaseMap.setBoxLabel(baseMap.getName());
				radioBaseMap.setFieldLabel(baseMap.getName());
				radiogroupBaseMap.add(radioBaseMap);
				panelBaseMaps.add(radioBaseMap);
				
				// Keep a reference to the basemap 
				baseMapPerRadio.put(radioBaseMap, baseMap);
			}
			
			radiogroupBaseMap.addListener(Events.Change, new Listener<FieldEvent>() {
				@Override
				public void handleEvent(FieldEvent be) {
					if (radiogroupBaseMap.getValue() != null) {
						setValue(baseMapPerRadio.get(radiogroupBaseMap.getValue()));
					}
				}
			});
			
			layout(true);
		}
	}


	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<BaseMap> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}


	@Override
	public BaseMap getValue() {
		return selectedBaseMap;
	}


	@Override
	public void setValue(BaseMap value) {
		this.selectedBaseMap=value;
		ValueChangeEvent.fire(this, value);
	}


	@Override
	public void setValue(BaseMap value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}
}
