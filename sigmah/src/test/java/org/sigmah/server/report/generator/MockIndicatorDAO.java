package org.sigmah.server.report.generator;

import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.domain.Indicator;

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
