package org.sigmah.client.page.entry.location;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.map.GoogleChartsIconBuilder;
import org.sigmah.client.page.entry.form.resources.SiteFormResources;
import org.sigmah.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.common.base.Strings;

/** 
 * Show a list of locations
 */
public class SearchResultListView extends ListView<LocationDTO> {
	
	
	public SearchResultListView(LocationSearchPresenter presenter) {
		super();
		
		setStore(presenter.getStore());
		setDisplayProperty("name");
		setTemplate(SiteFormResources.INSTANCE.locationTemplate().getText());
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
}
