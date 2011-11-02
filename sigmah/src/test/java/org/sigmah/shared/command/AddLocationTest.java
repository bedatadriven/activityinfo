package org.sigmah.shared.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.command.LocationDTOs;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.LocationDTO2;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class AddLocationTest extends CommandTestCase2 {
	@Test
	public void test() throws CommandException {
		LocationDTO2 location = LocationDTOs.newLocation();
		CreateResult result = execute(new AddLocation().setLocation(location));
		assertFalse("Expect result id not 0", result.getNewId()==0);
		
		GetLocations getLocations = new GetLocations().setName(location.getName());
		LocationsResult locations = execute(getLocations);

		LocationDTO2 newLocation = locations.getLocations().get(0);
		assertEquals("LocationName", location.getName(), newLocation.getName());
		assertEquals("LocationName", location.getAxe(), newLocation.getAxe());
		assertEquals("LocationName", location.getLongitude(), newLocation.getLongitude());
		assertEquals("LocationName", location.getLatitude(), newLocation.getLatitude());
	}
}
