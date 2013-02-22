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

import javax.xml.bind.annotation.XmlElement;

/*
 * Convenience class for layers using a circle to visualize indicators
 */
public abstract class CircledMapLayer extends PointMapLayer {

	private int minRadius = 8;
	private int maxRadius = 32;
	private double alpha = 0.75;
	private ScalingType scaling = ScalingType.Graduated;

	@XmlElement
	public int getMinRadius() {
	    return minRadius;
	}

	public void setMinRadius(int minRadius) {
	    this.minRadius = minRadius;
	}

	@XmlElement
	public int getMaxRadius() {
	    return maxRadius;
	}

	public void setMaxRadius(int maxRadius) {
	    this.maxRadius = maxRadius;
	}

	@XmlElement
	public double getAlpha() {
	    return alpha;
	}

	public void setAlpha(double alpha) {
	    this.alpha = alpha;
	}

	@XmlElement(defaultValue = "none")
	public ScalingType getScaling() {
	    return scaling;
	}

	public void setScaling(ScalingType scaling) {
	    this.scaling = scaling;
	}
}
