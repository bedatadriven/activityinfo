package org.sigmah.shared.report.model;


/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */


public class AttributeGroupDimension extends Dimension {
	
	private int attributeGroupId;

    public AttributeGroupDimension() {
		super(DimensionType.AttributeGroup );
	}

    public AttributeGroupDimension(int groupId) {
        super( DimensionType.AttributeGroup );
        this.attributeGroupId = groupId;
    }
	
	public AttributeGroupDimension(String caption, int groupId)  {
		super( DimensionType.AttributeGroup);
		this.attributeGroupId = groupId;
		set("caption", caption);
		set("id", "attrs_dim" + groupId);
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
        return this.attributeGroupId == that.attributeGroupId ;
    }

	@Override
	public int hashCode() {
		return attributeGroupId;
	}
	
	
}
