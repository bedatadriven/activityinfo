package org.activityinfo.server.command;

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/getreporttests.db.xml")
public class GetReportsTest extends CommandTestCase {

	@Test
	public void selectByUser1() {
		setUser(1);
		ReportsResult result = execute(new GetReports());
		assertNotNull(result);
		assertTrue(result.getData().size() == 2);
		assertEquals(1, result.getData().get(0).getId());
		assertEquals("Report 1", result.getData().get(0).getTitle());
		assertEquals("Alex", result.getData().get(0).getOwnerName());
		assertEquals(3, result.getData().get(1).getId());
		assertEquals("Report 3", result.getData().get(1).getTitle());
		assertEquals("Alex", result.getData().get(1).getOwnerName());
	}

	@Test
	public void selectByUser2() {
		setUser(2);
		ReportsResult result = execute(new GetReports());
		assertNotNull(result);
		assertThat(result.getData().size(), equalTo(1));
		assertEquals(2, result.getData().get(0).getId());
		assertEquals("Report 1", result.getData().get(0).getTitle());
		assertEquals("Bavon", result.getData().get(0).getOwnerName());
	}

}
