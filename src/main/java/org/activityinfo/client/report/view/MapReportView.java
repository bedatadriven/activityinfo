package org.activityinfo.client.report.view;

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

import org.activityinfo.client.map.GoogleMapsReportOverlays;
import org.activityinfo.client.widget.GoogleMapsPanel;
import org.activityinfo.shared.report.model.MapReportElement;

import com.extjs.gxt.ui.client.widget.Component;

public class MapReportView extends GoogleMapsPanel implements
    ReportView<MapReportElement> {

    private GoogleMapsReportOverlays overlays;
    private MapReportElement element;

    public MapReportView() {
        setHeaderVisible(false);
    }

    @Override
    protected void onMapInitialized() {
        overlays = new GoogleMapsReportOverlays(getMapWidget());
        if (element != null) {
            addContent();
        }
    }

    @Override
    public void show(MapReportElement element) {
        this.element = element;
        if (isMapLoaded()) {
            addContent();
        }
    }

    private void addContent() {
        overlays.syncWith(element);
    }

    @Override
    public Component asComponent() {
        return this;
    }

}
