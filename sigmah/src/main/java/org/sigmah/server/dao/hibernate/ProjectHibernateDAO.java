package org.sigmah.server.dao.hibernate;

import javax.persistence.EntityManager;

import org.sigmah.shared.dao.ProjectDAO;
import org.sigmah.shared.domain.Project;

import com.google.inject.Inject;

public class ProjectHibernateDAO extends GenericDAO<Project, Integer> implements ProjectDAO {

	@Inject
	protected ProjectHibernateDAO(EntityManager em) {
		super(em);
	}
}