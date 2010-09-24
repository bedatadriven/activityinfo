package org.sigmah.shared.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.sigmah.shared.domain.layout.Layout;

/**
 * Project details entity.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = "project_details")
public class ProjectDetails implements Serializable {

	private static final long serialVersionUID = -1266259112071917788L;

	private Long id;
	private ProjectModel projectModel;
	private Layout layout;

	@OneToOne
	@JoinColumn(name = "id_project_model")
	public ProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(ProjectModel projectModel) {
		this.projectModel = projectModel;
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_layout", nullable = false)
	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
