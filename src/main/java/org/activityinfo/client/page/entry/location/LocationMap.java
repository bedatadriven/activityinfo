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

import java.util.List;

import org.activityinfo.client.Log;
import org.activityinfo.client.page.entry.form.resources.SiteFormResources;
import org.activityinfo.client.util.LeafletUtil;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.map.MapboxLayers;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.util.mapping.Extents;
import org.discotools.gwt.leaflet.client.LeafletResourceInjector;
import org.discotools.gwt.leaflet.client.Options;
import org.discotools.gwt.leaflet.client.crs.epsg.EPSG3857;
import org.discotools.gwt.leaflet.client.events.Event;
import org.discotools.gwt.leaflet.client.events.MouseEvent;
import org.discotools.gwt.leaflet.client.events.handler.EventHandler;
import org.discotools.gwt.leaflet.client.events.handler.EventHandlerManager;
import org.discotools.gwt.leaflet.client.layers.ILayer;
import org.discotools.gwt.leaflet.client.layers.others.LayerGroup;
import org.discotools.gwt.leaflet.client.layers.raster.TileLayer;
import org.discotools.gwt.leaflet.client.map.Map;
import org.discotools.gwt.leaflet.client.map.MapOptions;
import org.discotools.gwt.leaflet.client.marker.Marker;
import org.discotools.gwt.leaflet.client.types.DivIcon;
import org.discotools.gwt.leaflet.client.types.DivIconOptions;
import org.discotools.gwt.leaflet.client.types.LatLng;
import org.discotools.gwt.leaflet.client.types.LatLngBounds;
import org.discotools.gwt.leaflet.client.types.Point;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Html;
import com.google.common.collect.Lists;
import com.google.gwt.resources.client.ImageResource;


public class LocationMap extends Html {

    private final LocationSearchPresenter searchPresenter;
    private final NewLocationPresenter newLocationPresenter;

    private LayerGroup markerLayer;
    private Marker newLocationMarker;
    
    private Map map;

    public LocationMap(LocationSearchPresenter presenter,
        NewLocationPresenter newLocationPresenter) {
        super();
        this.searchPresenter = presenter;
        this.newLocationPresenter = newLocationPresenter;
        
        LeafletResourceInjector.ensureInjected();
        
        setStyleName("gwt-Map");   
        setHtml("<div style=\"width:100%; height: 100%; position: relative;\"></div>");
    }
    
    @Override
    protected void afterRender() {
    	super.afterRender();

    	Extents countryBounds = searchPresenter.getCountry().getBounds();

    	MapOptions mapOptions = new MapOptions();
    	mapOptions.setCenter(new LatLng(countryBounds.getCenterY(), countryBounds.getCenterX()));
    	mapOptions.setZoom(6);
    	mapOptions.setProperty("crs", new EPSG3857());

        TileLayer baseLayer = new TileLayer(MapboxLayers.MAPBOX_STREETS, new Options());
        
        markerLayer = new LayerGroup(new ILayer[0]);
        
        map = new Map(getElement().getElementsByTagName("div").getItem(0), mapOptions);
        map.addLayer(baseLayer);
        map.addLayer(markerLayer);
    		
    	bindEvents();
    }

    @Override
    protected void onResize(int width, int height) {
        super.onResize(width, height);
        map.invalidateSize(false);
    }
    
    private void bindEvents() {

        searchPresenter.getStore().addStoreListener(
            new StoreListener<LocationDTO>() {

                @Override
                public void storeDataChanged(StoreEvent<LocationDTO> event) {
                    updateSearchMarkers();
                }
            });
        searchPresenter.addListener(Events.Select,
            new Listener<LocationEvent>() {

                @Override
                public void handleEvent(LocationEvent event) {
                    if (event.getSource() != LocationMap.this) {
                        onLocationSelected(event.getLocation());
                    }
                }
            });

        newLocationPresenter.addListener(
            NewLocationPresenter.ACTIVE_STATE_CHANGED,
            new Listener<BaseEvent>() {
                @Override
                public void handleEvent(BaseEvent be) {
                    onModeChanged();
                }
            });

        newLocationPresenter.addListener(NewLocationPresenter.POSITION_CHANGED,
            new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    onNewLocationPosChanged();
                }
            });

        newLocationPresenter.addListener(NewLocationPresenter.BOUNDS_CHANGED,
            new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    if (newLocationPresenter.isActive()) {
                        LatLngBounds newBounds = LeafletUtil.newLatLngBounds(newLocationPresenter.getBounds());
                        map.fitBounds(newBounds);
                    }
                }
            });
        
    }

    private void updateSearchMarkers() {

        markerLayer.clearLayers();
        
        List<LocationDTO> locations = Lists.reverse(searchPresenter.getStore().getModels());
        LatLngBounds bounds = new LatLngBounds();

        boolean empty = true;        
        for (LocationDTO location : locations) {
            if (location.hasCoordinates()) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    
                Marker marker = createMarker(latLng, location.getMarker());
                markerLayer.addLayer(marker);
                
                bounds.extend(latLng);    
                bindClickEvent(location, marker);   
                
                empty = false;
            }
        }
        
        if (empty) {
            if(searchPresenter.getBounds() != null) {
                bounds = LeafletUtil.newLatLngBounds(searchPresenter.getBounds());
            } else {
                bounds = LeafletUtil.newLatLngBounds(searchPresenter.getCountry().getBounds());
            }
        } 
        int effectiveZoom = Math.min(8, map.getBoundsZoom(bounds, false));
        map.setView(bounds.getCenter(), effectiveZoom, false);
        map.fitBounds(bounds);
        
    }

    private void bindClickEvent(final LocationDTO location, Marker marker) {
        EventHandlerManager.addEventHandler(marker, 
        org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events.click, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                searchPresenter.select(this, location);
            }
        });
    }

    private Marker createMarker(LatLng latLng, String label) {
        DivIcon icon = createIcon(label);
        
        Options markerOptions = new Options();
        markerOptions.setProperty("icon", icon);
        
        Marker marker = new Marker(latLng, markerOptions);
        return marker;
    }

    private DivIcon createIcon(String label) {
        ImageResource markerImage = SiteFormResources.INSTANCE.blankMarker();

        DivIconOptions iconOptions = new DivIconOptions();
        iconOptions.setClassName(SiteFormResources.INSTANCE.style().locationMarker());
        iconOptions.setIconSize(new Point(
            markerImage.getWidth(),
            markerImage.getHeight()));
        iconOptions.setIconAnchor(new Point(
            markerImage.getWidth()/2, 
            markerImage.getHeight()));
        iconOptions.setHtml(label);
        
        DivIcon icon = new DivIcon(iconOptions);
        return icon;
    }

    private void onLocationSelected(LocationDTO location) {
        if (location != null && location.hasCoordinates()) {
            map.panTo(new LatLng(location.getLatitude(), location.getLongitude()));
        }
    }

    private void onModeChanged() {
        if (newLocationPresenter.isActive()) {
            if (newLocationMarker == null) {
                createNewLocationMarker();
            }
            newLocationMarker.setOpacity(1);
            panToNewLocation();
        } else if (newLocationMarker != null) {
            newLocationMarker.setOpacity(0);
        }
    }

    private void createNewLocationMarker() {
        DivIcon icon = createIcon("");
        
        Options markerOptions = new Options();
        markerOptions.setProperty("icon", icon);
        markerOptions.setProperty("draggable", true);
       
        newLocationMarker = new Marker(newLatLng(newLocationPresenter.getLatLng()), markerOptions);
        
        EventHandlerManager.addEventHandler(newLocationMarker, 
            org.discotools.gwt.leaflet.client.events.handler.EventHandler.Events.dragend, new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    newLocationPresenter.setLatLng(new AiLatLng(
                        newLocationMarker.getLatLng().lat(),
                        newLocationMarker.getLatLng().lng()));
                }
        });
        
        map.addLayer(newLocationMarker);
    }

    private LatLng newLatLng(AiLatLng latLng) {
        return new LatLng(latLng.getLat(), latLng.getLng());
    }

    private void onNewLocationPosChanged() {
        if (newLocationMarker != null) {
            Log.debug("New marker pos: " + newLocationPresenter.getLatLng());
            newLocationMarker.setLatLng(newLatLng(newLocationPresenter.getLatLng()));
        }
    }

    private void panToNewLocation() {
        if (!map.getBounds().contains(
            newLocationMarker.getLatLng())) {
            map.panTo(newLocationMarker.getLatLng());
        }
    }
}
