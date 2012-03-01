package org.sigmah.server.report.generator;

import org.sigmah.server.database.hibernate.dao.IndicatorDAO;
import org.sigmah.server.database.hibernate.entity.Indicator;

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
