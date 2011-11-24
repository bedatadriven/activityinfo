package org.sigmah.shared.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.command.LocationDTOs;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.LocationResult;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class AddLocationTest extends CommandTestCase2 {
	@Test
	public void test() throws CommandException {
		LocationDTO location = LocationDTOs.newLocation();
		CreateResult result = execute(new AddLocation().setLocation(location));
		assertFalse("Expect result id not 0", result.getNewId()==0);
		
		SearchLocations getLocations = new SearchLocations().setName(location.getName());
		LocationResult locations = execute(getLocations);

		LocationDTO newLocation = locations.getData().get(0);
		assertEquals(location.getName(), newLocation.getName());
		assertEquals(location.getAxe(), newLocation.getAxe());
		assertEquals(location.getLongitude(), newLocation.getLongitude());
		assertEquals(location.getLatitude(), newLocation.getLatitude());
	}
}
