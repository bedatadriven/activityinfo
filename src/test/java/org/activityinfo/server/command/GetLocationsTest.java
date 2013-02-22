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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetLocations;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetLocationsTest extends CommandTestCase2 {

	@Test
	public void getLocation() {
		
		setUser(1);
		
		LocationDTO location = execute(new GetLocations(1)).getLocation();
		
		assertThat(location, notNullValue());
		assertThat(location.getName(), equalTo("Penekusu Kivu"));
		assertThat(location.getAxe(), nullValue());
		assertThat(location.getAdminEntity(1).getName(), equalTo("Sud Kivu"));
		assertThat(location.getAdminEntity(2).getName(), equalTo("Shabunda"));
				
	}
	
}
