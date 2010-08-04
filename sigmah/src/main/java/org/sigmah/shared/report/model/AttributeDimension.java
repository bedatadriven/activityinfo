package org.sigmah.shared.report.model;

/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */


public class AttributeDimension extends Dimension {
	
	private int attributeId;
	
	public AttributeDimension() {
		super();
	}
	
	public AttributeDimension(int attributeId) {
		super( DimensionType.Attribute);
		this.attributeId = attributeId;
    }


    public int getAttributeId() {
		return attributeId;
	}


	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	@Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof AttributeDimension)) {
            return false;
        }
        AttributeDimension that = (AttributeDimension)other;
        if (this.attributeId == that.attributeId) {
        	return true;
        } else {
        	return false;
        }
    }
}
