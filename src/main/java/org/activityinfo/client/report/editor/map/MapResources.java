package org.activityinfo.client.report.editor.map;

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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MapResources extends ClientBundle {

    static final MapResources INSTANCE = GWT.create(MapResources.class);

    @Source("LayerTemplate.html")
    TextResource layerTemplate();

    @NotStrict
    @Source("MapStyle.css")
    MapStyle style();

    @NotStrict
    @Source("BaseMapDialog.css")
    BaseMapDialogStyle baseMapDialogStyle();

    @Source("GrabSprite.png")
    ImageResource grabSprite();

    @Source("Poi.png")
    ImageResource poi();

    @Source("RemoveLayer.png")
    ImageResource removeLayer();

    @Source("Icon.png")
    ImageResource icon();

    @Source("Piechart.png")
    ImageResource piechart();

    @Source("Bubble.png")
    ImageResource bubble();

    @Source("indicator.png")
    ImageResource singleSelect();

    @Source("indicators.png")
    ImageResource multiSelect();

    @Source("AddLayer.png")
    ImageResource addLayer();

    @Source("Error.png")
    ImageResource error();

    @Source("Layers.png")
    ImageResource layers();

    @Source("globe.png")
    ImageResource globe();

    @Source("Style.png")
    ImageResource styleIcon();

    @Source("Cluster.png")
    ImageResource clusterIcon();

    interface MapStyle extends CssResource {
    }

    interface BaseMapDialogStyle extends CssResource {
    }

}
