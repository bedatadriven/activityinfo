package org.sigmah.shared.command;

import static org.junit.Assert.assertEquals;

import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.SearchLocations;
import org.activityinfo.shared.command.result.LocationResult;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.command.LocationDTOs;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class CreateLocationTest extends CommandTestCase2 {
	@Test
	public void test() throws CommandException {
		LocationDTO location = LocationDTOs.newLocation();
		execute(new CreateLocation(location));
		
		SearchLocations getLocations = new SearchLocations().setName(location.getName());
		LocationResult locations = execute(getLocations);

		LocationDTO newLocation = locations.getData().get(0);
		assertEquals(location.getName(), newLocation.getName());
		assertEquals(location.getAxe(), newLocation.getAxe());
		assertEquals(location.getLongitude(), newLocation.getLongitude());
		assertEquals(location.getLatitude(), newLocation.getLatitude());
	}
}
