package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.layout.Layout;

/**
 * Report entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "report")
public class Report implements Serializable {

	private static final long serialVersionUID = -1266259112071917788L;

	private Long id;
	private PhaseModel phaseModel;
	private Layout layout;

	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "id_layout", nullable = false)
	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_report")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne(optional = false)
	@JoinColumn(name = "id_phase_model", nullable = false)
	public PhaseModel getPhaseModel() {
		return phaseModel;
	}

	public void setPhaseModel(PhaseModel phaseModel) {
		this.phaseModel = phaseModel;
	}

}
