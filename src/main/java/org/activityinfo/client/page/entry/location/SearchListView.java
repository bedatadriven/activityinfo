package org.activityinfo.client.page.entry.location;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Arrays;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.map.GoogleChartsIconBuilder;
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.shared.dto.LocationDTO;

import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
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

        getSelectionModel().addSelectionChangedListener(
            new SelectionChangedListener<LocationDTO>() {
                @Override
                public void selectionChanged(
                    SelectionChangedEvent<LocationDTO> se) {
                    presenter.select(this, se.getSelectedItem());
                }
            });

        addListener(Events.DoubleClick,
            new Listener<ListViewEvent<LocationDTO>>() {

                @Override
                public void handleEvent(ListViewEvent<LocationDTO> be) {
                    presenter.accept();
                }
            });

        presenter.addListener(Events.Select, new Listener<LocationEvent>() {

            @Override
            public void handleEvent(LocationEvent event) {
                if (event.getSource() != SearchListView.this) {
                    onResultSelected(event);
                }
            }
        });

        SiteFormResources.INSTANCE.style().ensureInjected();
    }

    private LocationDTO prepareUrl(LocationDTO model) {
        if (Strings.isNullOrEmpty(model.getMarker())) {
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
        if (index >= 0) {
            Element element = getElement(index);
            if (element != null) {
                element.scrollIntoView();
            }
        }
    }
}
