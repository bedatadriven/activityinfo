/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import javax.persistence.*;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class AttributeValue implements java.io.Serializable {

	private AttributeValueId id;
	private Attribute attribute;
	private Site site;
	private boolean value;

    public AttributeValue() {
	}


	public AttributeValue(Site site, Attribute attribute, boolean value) {
		setId(new AttributeValueId(site.getId(), attribute.getId()));
		setSite(site);
		setAttribute(attribute);
		setValue(value);
	}


	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "siteId", column = @Column(name = "SiteId", nullable = false)),
		@AttributeOverride(name = "attributeId", column = @Column(name = "AttributeId", nullable = false)) })
		public AttributeValueId getId() {
		return this.id;
	}

	public void setId(AttributeValueId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AttributeId", nullable = false, insertable = false, updatable = false)
	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SiteId", nullable = false, insertable = false, updatable = false)
	public Site getSite() {
		return this.site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Column(name = "Value")
	public boolean getValue() {
		return this.value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}


}
