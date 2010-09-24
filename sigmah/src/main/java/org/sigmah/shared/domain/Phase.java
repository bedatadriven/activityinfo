package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Phase entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "phase")
public class Phase implements Serializable {

	private static final long serialVersionUID = -7265918761740982615L;

	private Long id;
	private PhaseModel model;
	private Project parentProject;
	private Date startDate;
	private Date endDate;

	public Phase() {

	}

	public Phase(PhaseModel model) {
		this.model = model;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_phase")
	public Long getId() {
		return id;
	}

	@ManyToOne(optional = false)
	@JoinColumn(name = "id_project", nullable = false)
	public Project getParentProject() {
		return parentProject;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = true)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = true)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@OneToOne
	@JoinColumn(name = "id_phase_model", nullable = false)
	public PhaseModel getModel() {
		return model;
	}

	public void setModel(PhaseModel model) {
		this.model = model;
	}

	/**
	 * Starts a phase.
	 */
	public void start() {
		startDate = new Date();
	}

	/**
	 * Returns if the phase is active (start date isn't <code>null</code>).
	 *
	 * @return If the phase is active.
	 */
	@Transient
	public boolean isActive() {
		return startDate != null;
	}
}
