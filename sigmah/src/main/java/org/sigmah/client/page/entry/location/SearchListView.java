package org.sigmah.client.page.entry.location;

import java.util.Arrays;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.GoogleChartsIconBuilder;
import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.common.base.Strings;
import com.google.gwt.user.client.Element;

/** 
 * Show a list of locations
 */
public class SearchListView extends ListView<LocationDTO> {
	
	
	public SearchListView(final LocationSearchPresenter presenter) {
		super();
		
		setStore(presenter.getStore());
		setDisplayProperty("name");
		setTemplate(SiteFormResources.INSTANCE.locationTemplate().getText());
		addStyleName(SiteFormResources.INSTANCE.style().locationSearchResults());
		setItemSelector(".locSerResult");
		setBorders(false);
		setStyleAttribute("overflow", "visible");
		setLoadingText(I18N.CONSTANTS.loading());
		setModelProcessor(new ModelProcessor<LocationDTO>() {
			
			@Override
			public LocationDTO prepareData(LocationDTO model) {
				return prepareUrl(model);
			}
		});

		getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<LocationDTO>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<LocationDTO> se) { 
				presenter.select(this, se.getSelectedItem());
			}
		});
		
		presenter.addListener(Events.Select, new Listener<LocationEvent>() {

			@Override
			public void handleEvent(LocationEvent event) {
				if(event.getSource() != SearchListView.this) {
					onResultSelected(event);
				}
			}
		});

		SiteFormResources.INSTANCE.style().ensureInjected();
	}


	private LocationDTO prepareUrl(LocationDTO model) {
		if(Strings.isNullOrEmpty(model.getMarker())) {
			GoogleChartsIconBuilder builder = new GoogleChartsIconBuilder();
			builder.setLabel("?");
			builder.setPrimaryColor("BBBBBB");
			model.set("markerUrl", builder.composePinUrl());
		} else {
			GoogleChartsIconBuilder builder = new GoogleChartsIconBuilder();
			builder.setLabel(model.getMarker());
			model.set("markerUrl", builder.composePinUrl());
		}
		return model;
	}
	
	/**
	 * Location result was selected externally
	 */
	private void onResultSelected(LocationEvent event) {
		select(event.getLocation());
		scrollIntoView(event.getLocation());
	}
	
	private void select(LocationDTO location) {
		getSelectionModel().setSelection(Arrays.asList(location));
	}
	
	private void scrollIntoView(LocationDTO location) {
		int index = store.indexOf(location);
		if(index >= 0) {
			Element element = getElement(index);
			if(element != null) {
				element.scrollIntoView();
			}
		}
	}
}
