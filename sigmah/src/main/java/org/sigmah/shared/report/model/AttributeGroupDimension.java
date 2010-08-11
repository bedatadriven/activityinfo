package org.sigmah.shared.report.model;

import java.util.List;

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */


public class AttributeGroupDimension extends Dimension {
	
	private int attributeGroupId;
	private List <Integer> attributeIds;
	
	public AttributeGroupDimension() {
		super();
	}
	
	public AttributeGroupDimension(String name, int groupId, List< Integer > attributeIds)  {
		super( DimensionType.AttributeGroup);
		this.attributeGroupId = groupId;
		this.attributeIds = attributeIds;
		set("caption", name);
		set("id", "attrs_dim" + groupId);
		
    }

	public List<Integer> getAttributeIds() {
		return attributeIds;
	}

	public void setAttributeIds(List<Integer> attributeIds) {
		this.attributeIds = attributeIds;
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
        AttributeGroupDimension that = (AttributeGroupDimension)other;
        if (this.attributeGroupId == that.attributeGroupId) {
        	return true;
        } else {
        	return false;
        }
    }
}
