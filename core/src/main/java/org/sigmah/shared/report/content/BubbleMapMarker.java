/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

import java.util.HashSet;
import java.util.Set;

import org.sigmah.shared.report.model.clustering.Clustering;

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
