/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.PieMapMarker;
import org.sigmah.shared.report.content.Point;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointValue {
    public SiteData site;
    public MapSymbol symbol;
    public double value;
    public Point px;
    public Rectangle iconRect;
    public List<PieMapMarker.SliceValue> slices;
	private Map<Integer, Integer> adminMembership = new HashMap<Integer, Integer>();

    public PointValue() {
    }

    public PointValue(SiteData site, MapSymbol symbol, double value, Point px) {
        this.site = site;
        this.symbol = symbol;
        this.value = value;
        this.px = px;
    }

    public PointValue(SiteData site, Point px, Rectangle iconRect) {
        this.site = site;
        this.px = px;
        this.iconRect = iconRect;
        this.value = 1;
    }

	public void setAdminMembership(Map<Integer, Integer> adminMembership) {
		this.adminMembership = adminMembership;
	}

	public Map<Integer, Integer> getAdminMembership() {
		return adminMembership;
	}
}