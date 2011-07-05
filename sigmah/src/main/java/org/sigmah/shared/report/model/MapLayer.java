/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author Alex Bertram
 */
public abstract class MapLayer implements Serializable {
	private boolean isVisible = true;

	//@XmlElement
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
