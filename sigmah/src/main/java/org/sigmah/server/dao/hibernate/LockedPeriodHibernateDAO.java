package org.sigmah.server.dao.hibernate;

import javax.persistence.EntityManager;

import org.sigmah.shared.dao.LockedPeriodDAO;
import org.sigmah.shared.domain.LockedPeriod;

public class LockedPeriodHibernateDAO extends GenericDAO<LockedPeriod, Integer> implements LockedPeriodDAO {

	protected LockedPeriodHibernateDAO(EntityManager em) {
		super(em);
	}

}