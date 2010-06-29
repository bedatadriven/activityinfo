/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.report.content;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SimpleCategory implements DimensionCategory {

    private String label;

    private SimpleCategory() {

    }

    public SimpleCategory(String label) {
        this.label = label;
    }

    @Override
    public Comparable getSortKey() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SimpleCategory that = (SimpleCategory) o;

        if (label != null ? !label.equals(that.label) : that.label != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return label != null ? label.hashCode() : 0;
    }
}
