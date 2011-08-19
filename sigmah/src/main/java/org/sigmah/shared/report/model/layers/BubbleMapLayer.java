/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model.layers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines a graduated symbol layer where indicators are visualized using a circle
 */
public class BubbleMapLayer extends CircledMapLayer {

    private List<Dimension> colorDimensions = new ArrayList<Dimension>();
    private String bubbleColor = "FF0000";
    private String labelColor = "000000";

    public BubbleMapLayer() {
    }

    public void addIndicator(int id) {
        indicatorIds.add(id);
    }

    public boolean containsIndicatorDimension() {
        for(Dimension dim : colorDimensions) {
            if(dim.getType() == DimensionType.Indicator) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @XmlElement(name="dimension")
    @XmlElementWrapper(name="colors")
    public List<Dimension> getColorDimensions() {
        return colorDimensions;
    }

    @Deprecated
    public void setColorDimensions(List<Dimension> colorDimensions) {
        this.colorDimensions = colorDimensions;
        
    }

    /**
     * 
     * @return the color used to draw the circles 
     */
    @XmlElement
    public String getBubbleColor() {
        return bubbleColor;
    }

    /**
     * 
     * @param color the color used to draw the circles
     */
    public void setBubbletColor(String color) {
        this.bubbleColor = color;
    }

    /**
     * 
     * @return the hex color used to label bubbles (for the text)
     */
    @XmlElement
    public String getLabelColor() {
        return labelColor;
    }

    /**
     * 
     * @param labelColor the color to use when labelling bubbles (for the text)
     */
    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

	@Override
	public boolean supportsMultipleIndicators() {
		return true;
	}

	@Override
	public String getTypeName() {
		return "Bubble";
	}
}
