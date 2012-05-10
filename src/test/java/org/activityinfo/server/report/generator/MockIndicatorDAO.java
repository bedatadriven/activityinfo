package org.activityinfo.server.report.generator;

import org.activityinfo.server.database.hibernate.dao.IndicatorDAO;
import org.activityinfo.server.database.hibernate.entity.Indicator;

public class MockIndicatorDAO implements IndicatorDAO {

	@Override
	public void persist(Indicator entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public Indicator findById(Integer primaryKey) {
		// TODO Auto-generated method stub
		return null;
	}

}
