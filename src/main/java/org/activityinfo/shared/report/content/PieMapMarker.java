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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PieMapMarker extends BubbleMapMarker {

    public static class SliceValue implements Serializable {
        private double value;
        private DimensionCategory category;
        private String color;
        private int indicatorId = 0;

        public SliceValue() {
        }

        public SliceValue(SliceValue slice) {
            this.value = slice.value;
            this.category = slice.category;
            this.color = slice.color;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public DimensionCategory getCategory() {
            return category;
        }

        public void setCategory(DimensionCategory category) {
            this.category = category;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public int getIndicatorId() {
            return indicatorId;
        }

        public void setIndicatorId(int indicatorId) {
            this.indicatorId = indicatorId;
        }
    }

    public PieMapMarker() {
    }

    private List<SliceValue> slices = new ArrayList<SliceValue>(0);

    public List<SliceValue> getSlices() {
        return slices;
    }

    public void setSlices(List<SliceValue> slices) {
        this.slices = slices;
    }
}
