package org.sigmah.server.command;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.database.hibernate.entity.LockedPeriod;
import org.sigmah.shared.command.LockEntity;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.collect.Maps;

@RunWith(InjectionSupport.class)
public class LockedPeriodTest extends CommandTestCase {

	
	@Test
	@OnDataSet("/dbunit/sites-simple1.db.xml")
	public void createTest() throws CommandException {
		
		setUser(1);
		
		LockedPeriodDTO dto = new LockedPeriodDTO();
		dto.setName("my name");
		dto.setFromDate(new LocalDate(2011,1,1));
		dto.setToDate(new LocalDate(2011,1,31));
		dto.setEnabled(true);
		
		LockEntity create = new LockEntity(dto);
		create.setUserDatabaseId(1);
		
		CreateResult result = execute(create);
		
		Map<String, Object> changes = Maps.newHashMap();
		changes.put("toDate", new LocalDate(2011,2,28));
		
		execute(new UpdateEntity("LockedPeriod", result.getNewId(), changes));
		
	}
	
}
