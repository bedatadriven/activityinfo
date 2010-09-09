package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Privacy level entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "privacy_level")
public class PrivacyLevel implements Serializable {

	private static final long serialVersionUID = -233609776474193411L;

	private Integer level;

	@Id
	@Column(name = "privacy_level")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
