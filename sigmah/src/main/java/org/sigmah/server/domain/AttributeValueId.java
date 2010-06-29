/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author Alex Bertram
 *
 */
@Embeddable
public class AttributeValueId implements java.io.Serializable {

	private int siteId;
	private int attributeId;

	public AttributeValueId() {
	}

	public AttributeValueId(int siteId, int attributeId) {
		this.siteId = siteId;
		this.attributeId = attributeId;
	}

	@Column(name = "SiteId", nullable = false)
	public int getSiteId() {
		return this.siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	@Column(name = "AttributeId", nullable = false)
	public int getAttributeId() {
		return this.attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
            return true;
        }
		if ((other == null)) {
            return false;
        }
		if (!(other instanceof AttributeValueId)) {
            return false;
        }
		AttributeValueId castOther = (AttributeValueId) other;

		return (this.getSiteId() == castOther.getSiteId())
		&& (this.getAttributeId() == castOther.getAttributeId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSiteId();
		result = 37 * result + this.getAttributeId();
		return result;
	}

}