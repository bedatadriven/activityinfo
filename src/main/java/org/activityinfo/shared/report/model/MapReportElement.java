

package org.activityinfo.shared.report.model;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.map.BaseMap;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.model.layers.BubbleMapLayer;
import org.activityinfo.shared.report.model.layers.IconMapLayer;
import org.activityinfo.shared.report.model.layers.MapLayer;
import org.activityinfo.shared.report.model.layers.PiechartMapLayer;
import org.activityinfo.shared.report.model.layers.PolygonMapLayer;

import com.google.common.collect.Sets;

/**
 * Models a map element within a report
 */
public class MapReportElement extends ReportElement<MapContent> {
	
	public static final String AUTO_BASEMAP = "@AUTO@";
	
    private String baseMapId = AUTO_BASEMAP;
    private int width = 640;
    private int height = 480;
    private AiLatLng center = null;
    private int zoomLevel = -1;
    private List<MapLayer> layers = new ArrayList<MapLayer>(0);

    public MapReportElement() {
    }

    @XmlElement(name="tileBaseMap", required = true)
    public String getBaseMapId() {
        return baseMapId;
    }

    public void setBaseMapId(String baseMapId) {
        this.baseMapId = baseMapId;
    }

    @XmlElement(defaultValue="640")
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @XmlElement(defaultValue="480")
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void addLayer(MapLayer layer) {
        this.layers.add(layer);
    }

    @XmlElementWrapper(name="layers")
    @XmlElements({
        @XmlElement(name="bubbles", type=BubbleMapLayer.class),
        @XmlElement(name="pie", type=PiechartMapLayer.class),
        @XmlElement(name="icons", type=IconMapLayer.class),
        @XmlElement(name="polygons", type=PolygonMapLayer.class)
    })
    public List<MapLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<MapLayer> layers) {
        this.layers = layers;
    }
    
    public void setLayers(MapLayer... layers) {
    	this.layers = new ArrayList<MapLayer>();
    	this.layers.addAll(Arrays.asList(layers));
    }

    /*
     * Returns the center of the map
     * 
     * when null, ignored by the generator. Instead, the generator calculates the 
     * center by the set of locations on the map AdministrativeLevelClustering
     */
    @XmlTransient
	public AiLatLng getCenter() {
		return center;
	}

	public void setCenter(AiLatLng center) {
		this.center = center;
	}

	/*
	 * Returns the zoomlevel of the map background
	 * 
     * When null, ignored by the generator. Instead, the generator calculates the 
     * zoomlevel by the set of locations on the map and the size of the map
	 */
    @XmlTransient
	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	@Override
	@XmlTransient
	public Set<Integer> getIndicators() {
		Set<Integer> ids = Sets.newHashSet();
		for(MapLayer layer : layers) {
			ids.addAll(layer.getIndicatorIds());
		}
		return ids;
	}

	@Override
	public String toString() {
		return "MapReportElement [baseMapId=" + baseMapId + ", layers="
				+ layers + "]";
	}
}