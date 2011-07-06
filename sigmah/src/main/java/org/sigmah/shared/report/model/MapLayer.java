/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.sigmah.client.page.map.MapLayerModel;
import org.sigmah.shared.domain.Indicator;

/**
 * @author Alex Bertram
 */
public abstract class MapLayer implements Serializable {
	private boolean isVisible = true;
	private MapLayerModel model;
	private Indicator indicator;

	//@XmlElement
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setModel(MapLayerModel model) {
		this.model = model;
	}

	public MapLayerModel getModel() {
		return model;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public Indicator getIndicator() {
		return indicator;
	}
}
