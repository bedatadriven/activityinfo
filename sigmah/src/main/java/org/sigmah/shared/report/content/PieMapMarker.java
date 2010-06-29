/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

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
