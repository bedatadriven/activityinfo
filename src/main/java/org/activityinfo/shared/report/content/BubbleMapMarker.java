

package org.activityinfo.shared.report.content;

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

import java.util.HashSet;
import java.util.Set;

import org.activityinfo.shared.report.model.clustering.Clustering;

/**
 * @author Alex Bertram
 */
public class BubbleMapMarker extends MapMarker {
    private String color = "FF0000";
    private String label;
    private double alpha;
    private double value;
    private int radius;
    private String labelColor = "FF0000";
    private Set<Integer> indicatorIds = new HashSet<Integer>();
    private Clustering clustering;
    private int clusterAmount;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    @Override
    public int getSize() {
        return radius;
    }

	public Set<Integer> getIndicatorIds() {
		return indicatorIds;
	}

	public void setIndicatorIds(Set<Integer> indicatorIds) {
		this.indicatorIds = indicatorIds;
	}

	public void setClustering(Clustering clustering) {
		this.clustering = clustering;
	}

	public Clustering getClustering() {
		return clustering;
	}

	public void setClusterAmount(int clusterAmount) {
		this.clusterAmount = clusterAmount;
	}

	public int getClusterAmount() {
		return clusterAmount;
	}
}
