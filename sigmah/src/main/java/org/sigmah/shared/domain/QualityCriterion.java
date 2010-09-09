package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Quality criterion entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "quality_criterion")
public class QualityCriterion implements Serializable {

	private static final long serialVersionUID = -9015626378861486393L;

	private Long id;
	private String name;

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_quality_criterion")
	public Long getId() {
		return id;
	}

	@Column(name = "name", nullable = false, length = 1024)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
