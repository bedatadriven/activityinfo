package org.sigmah.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.activityinfo.shared.command.GetLocation;
import org.activityinfo.shared.dto.LocationDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetLocationTest extends CommandTestCase2 {

	@Test
	public void getLocation() {
		
		setUser(1);
		
		LocationDTO location = execute(new GetLocation(1));
		
		assertThat(location.getName(), equalTo("Penekusu Kivu"));
		assertThat(location.getAxe(), nullValue());
		assertThat(location.getAdminEntity(1).getName(), equalTo("Sud Kivu"));
		assertThat(location.getAdminEntity(2).getName(), equalTo("Shabunda"));
				
	}
	
}
