package org.activityinfo.server.command;

import java.util.Map;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.LockedPeriod;
import org.activityinfo.shared.command.CreateLockedPeriod;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

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
		
		CreateLockedPeriod create = new CreateLockedPeriod(dto);
		create.setUserDatabaseId(1);
		
		CreateResult result = execute(create);
		
		Map<String, Object> changes = Maps.newHashMap();
		changes.put("toDate", new LocalDate(2011,2,28));
		
		execute(new UpdateEntity("LockedPeriod", result.getNewId(), changes));
		
	}
	
}
