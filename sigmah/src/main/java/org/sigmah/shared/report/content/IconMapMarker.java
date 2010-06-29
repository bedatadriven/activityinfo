/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import org.sigmah.shared.report.model.MapIcon;

/**
 * @author Alex Bertram
 */
public class IconMapMarker extends MapMarker {
    

    private MapIcon icon;

    public MapIcon getIcon() {
        return icon;
    }

    public void setIcon(MapIcon icon) {
        this.icon = icon;
    }
}
