package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.MapContent;

import java.util.List;
import java.util.ArrayList;


/**
 * Models a map element within a report
 * 
 * @author Alex Bertram
 *
 */
public class MapElement extends ReportElement {

    private String baseMapId;
    private int width = 640;
    private int height = 480;
    private MapContent content;
    private List<MapLayer> layers = new ArrayList<MapLayer>(0);

    public MapElement() {
    }

    public String getBaseMapId() {
        return baseMapId;
    }

    public void setBaseMapId(String baseMapId) {
        this.baseMapId = baseMapId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public MapContent getContent() {
        return content;
    }

    public void setContent(MapContent content) {
        this.content = content;
    }

    public void addLayer(MapLayer layer) {
        this.layers.add(layer);
    }

    public List<MapLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<MapLayer> layers) {
        this.layers = layers;
    }
}
