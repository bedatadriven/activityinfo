/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.shared.report.content;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class PieMapMarker extends BubbleMapMarker {

    public static class Slice {
        private double value;
        private DimensionCategory category;
        private int color;

        public Slice() {
        }

        public Slice(Slice slice) {
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

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    public PieMapMarker() {
    }

    private List<Slice> slices = new ArrayList<Slice>(0);

    public List<Slice> getSlices() {
        return slices;
    }

    public void setSlices(List<Slice> slices) {
        this.slices = slices;
    }


}
