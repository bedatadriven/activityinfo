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
    private int defaultColor = 255; // blue
    private int labelColor;

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

    @XmlElement(name="dimension")
    @XmlElementWrapper(name="colors")
    public List<Dimension> getColorDimensions() {
        return colorDimensions;
    }

    public void setColorDimensions(List<Dimension> colorDimensions) {
        this.colorDimensions = colorDimensions;
        
    }

    @XmlElement
    public int getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
    }

    @XmlElement
    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
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
