package org.activityinfo.shared.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.Arrays;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.shared.command.GetReportVisibility;
import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.UpdateReportVisibility;
import org.activityinfo.shared.command.result.ReportVisibilityResult;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.dto.ReportVisibilityDTO;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class, ReportModule.class})
@OnDataSet("/dbunit/schema1.db.xml")
public class ReportVisibilityTest extends CommandTestCase2 {

	@Test
	public void update() {
		
		setUser(1);
		
		ReportVisibilityDTO db1 = new ReportVisibilityDTO();
		db1.setDatabaseId(1);
		db1.setVisible(true);
		db1.setDefaultDashboard(true);
		
		ReportVisibilityDTO db2 = new ReportVisibilityDTO();
		db2.setDatabaseId(2);
		db2.setVisible(false);
		
		ReportVisibilityDTO db3 = new ReportVisibilityDTO();
		db3.setDatabaseId(3);
		db3.setVisible(true);
		
		UpdateReportVisibility update = new UpdateReportVisibility(1, Arrays.asList(db1, db2, db3));
		execute(update);
		
		ReportVisibilityResult result = execute(new GetReportVisibility(1));
		assertThat(result.getList().size(), equalTo(2));
		
		// make sure we can still see the report
		
		ReportsResult visibleToMe = execute(new GetReports());
		assertThat(visibleToMe.getData().size(), equalTo(1));

		
		
		setUser(2); // Bavon
		
		ReportsResult visibleToBavon = execute(new GetReports());
		assertThat(visibleToBavon.getData().size(), equalTo(2));
		assertThat(getById(visibleToBavon, 1).isDashboard(), equalTo(true));
		assertThat(getById(visibleToBavon, 2).isDashboard(), equalTo(false));


		setUser(3); // Stefan, no access to db
		
		ReportsResult visibleToStefan = execute(new GetReports());
		assertThat(visibleToStefan.getData().size(), equalTo(0));
		
		
	}
	
	private ReportMetadataDTO getById(ReportsResult result, int id) {
		for(ReportMetadataDTO dto : result.getData()) {
			if(dto.getId() == id) {
				return dto;
			}
		}
		throw new AssertionError("no report with id " + id);
	}
}
