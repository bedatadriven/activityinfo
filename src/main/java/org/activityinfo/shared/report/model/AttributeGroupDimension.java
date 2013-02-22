package org.activityinfo.shared.report.model;

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

public class AttributeGroupDimension extends Dimension {

    private int attributeGroupId;

    public AttributeGroupDimension() {
        super(DimensionType.AttributeGroup);
    }

    public AttributeGroupDimension(int groupId) {
        super(DimensionType.AttributeGroup);
        this.attributeGroupId = groupId;
    }

    public int getAttributeGroupId() {
        return attributeGroupId;
    }

    public void setAttributeGroupId(int attributeGroupId) {
        this.attributeGroupId = attributeGroupId;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof AttributeGroupDimension)) {
            return false;
        }
        AttributeGroupDimension that = (AttributeGroupDimension) other;
        return this.attributeGroupId == that.attributeGroupId;
    }

    @Override
    public int hashCode() {
        return attributeGroupId;
    }
}
