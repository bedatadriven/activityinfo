package org.sigmah.shared.command;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.GetLocations;
import org.sigmah.shared.command.GetLocations.LocationsResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetLocationsHandlerTest extends CommandTestCase2 {

	
	@Test 
	public void test() throws CommandException {
		GetLocations getLocations = new GetLocations().setAdminEntityIds(Arrays.asList(3, 12)).setName("Sh");
		LocationsResult result =  execute(getLocations);
		assertTrue(result.getLocations().size() == 1);
										
	}
}
