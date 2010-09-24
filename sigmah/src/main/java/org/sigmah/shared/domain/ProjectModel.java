package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.element.FlexibleElement;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * Project model entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "project_model")
public class ProjectModel extends BaseModelData implements Serializable {

	private static final long serialVersionUID = -1266259112071917788L;

	private Long id;
	private String name;
	private PhaseModel rootPhase;
	private List<PhaseModel> phases = new ArrayList<PhaseModel>();
	private List<FlexibleElement> elements = new ArrayList<FlexibleElement>();
	private ProjectBanner projectBanner;
	private ProjectDetails projectDetails;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_project_model")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name", nullable = false, length = 512)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(optional = true)
	@JoinColumn(name = "id_root_phase_model", nullable = true)
	public PhaseModel getRootPhase() {
		return rootPhase;
	}

	public void setRootPhase(PhaseModel rootPhase) {
		this.rootPhase = rootPhase;
	}

	@OneToMany(mappedBy = "parentProjectModel", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public List<PhaseModel> getPhases() {
		return phases;
	}

	public void setPhases(List<PhaseModel> phases) {
		this.phases = phases;
	}

	public void addPhase(PhaseModel phase) {
		if (phase != null) {
			phase.setParentProjectModel(this);
			phases.add(phase);
		}
	}

	public void setElements(List<FlexibleElement> elements) {
		this.elements = elements;
	}

	@OneToMany(mappedBy = "parentProjectModel", cascade = CascadeType.ALL)
	public List<FlexibleElement> getElements() {
		return elements;
	}

	@OneToOne(mappedBy = "projectModel", cascade = CascadeType.ALL)
	public ProjectBanner getProjectBanner() {
		return projectBanner;
	}

	public void setProjectBanner(ProjectBanner projectBanner) {
		this.projectBanner = projectBanner;
	}

	@OneToOne(mappedBy = "projectModel", cascade = CascadeType.ALL)
	public ProjectDetails getProjectDetails() {
		return projectDetails;
	}

	public void setProjectDetails(ProjectDetails projectDetails) {
		this.projectDetails = projectDetails;
	}

	public void addFlexibleElement(FlexibleElement element) {
		if (element != null) {
			element.setParentProjectModel(this);
			elements.add(element);
		}
	}

	@Override
	public String toString() {
		
		return this.id.toString();
		
		/*
		final StringBuilder sb = new StringBuilder();

		sb.append(this.getClass().getSimpleName());
		sb.append(" (");
		sb.append("id -> ");
		sb.append(id);
		sb.append(", ");
		sb.append("name -> ");
		sb.append(name);
		sb.append(", ");
		sb.append("rootPhase -> ");
		sb.append(rootPhase != null ? rootPhase.getId() : "null");
		sb.append(")\n\n");
		sb.append("phases count -> ");
		sb.append(phases.size());
		sb.append("\n\n");

		int i = 1;
		for (final PhaseModel phase : phases) {
			sb.append("- phase ");
			sb.append(i);
			sb.append(": ");
			sb.append(phase);
			sb.append("\n");
			i++;
		}

		sb.append("elements count -> ");
		sb.append(elements.size());
		sb.append("\n\n");

		i = 1;
		for (final FlexibleElement element : elements) {
			sb.append("- element ");
			sb.append(i);
			sb.append(": ");
			sb.append(element);
			sb.append("\n");
			i++;
		}

		return sb.toString();*/
	}

}
