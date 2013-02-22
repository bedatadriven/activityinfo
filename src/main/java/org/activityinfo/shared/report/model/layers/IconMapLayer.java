

package org.activityinfo.shared.report.model.layers;

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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.model.MapIcon;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.Clustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;
import org.activityinfo.shared.report.model.labeling.ArabicNumberSequence;
import org.activityinfo.shared.report.model.labeling.LabelSequence;
import org.activityinfo.shared.report.model.labeling.LatinAlphaSequence;

/*
 * Displays an icon on a point of interest
 */
public class IconMapLayer extends PointMapLayer {
    
	private List<Integer> activityIds = new ArrayList<Integer>(0);
    
	// Set the first found icon as default (top declared icon)
	private String icon = MapIcon.Icon.Default.name();


    public IconMapLayer() {
    }

    @XmlElement(name="activity")
    @XmlElementWrapper(name="activities")
    public List<Integer> getActivityIds() {
        return activityIds;
    }

    public void setActivityIds(List<Integer> activityIds) {
        this.activityIds = activityIds;
    }

    @XmlElement
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void addActivityId(int activityId) {
        activityIds.add(activityId);
    }

	@Override
	public boolean supportsMultipleIndicators() {
		return false;
	}

	@Override
	public String getTypeName() {
		return "Icon";
	}

	@Override
	public String toString() {
		return "IconMapLayer [indicatorIds=" + getIndicatorIds() + ", icon=" + icon
				+ "]";
	}
	
	
}
