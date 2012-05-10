/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.content;

import org.activityinfo.shared.report.model.MapIcon;

/**
 * @author Alex Bertram
 */
public class IconMapMarker extends MapMarker {
    
    private int indicatorId = 0;
    private MapIcon icon;

    public MapIcon getIcon() {
        return icon;
    }

    public void setIcon(MapIcon icon) {
        this.icon = icon;
    }

	public int getIndicatorId() {
		return indicatorId;
	}

	public void setIndicatorId(int indicatorId) {
		this.indicatorId = indicatorId;
	}
    
    
}
