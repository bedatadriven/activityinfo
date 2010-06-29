/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.MapContent;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import java.util.ArrayList;
import java.util.List;


/**
 * Models a map element within a report
 * 
 * @author Alex Bertram
 *
 */
public class MapElement extends ReportElement<MapContent> {


    private String baseMapId;
    private int width = 640;
    private int height = 480;
    private List<MapLayer> layers = new ArrayList<MapLayer>(0);

    public MapElement() {
    }

    @XmlElement(name="baseMap", required = true)
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
            @XmlElement(name="bubbles", type= BubbleMapLayer.class),
            @XmlElement(name="icons", type=IconMapLayer.class)
    })
    public List<MapLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<MapLayer> layers) {
        this.layers = layers;
    }


}
