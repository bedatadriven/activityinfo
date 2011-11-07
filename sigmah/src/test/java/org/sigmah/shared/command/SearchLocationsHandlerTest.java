package org.sigmah.shared.command;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.result.LocationResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchLocationsHandlerTest extends CommandTestCase2 {

	
	@Test 
	public void test() throws CommandException {
		SearchLocations getLocations = new SearchLocations().setAdminEntityIds(Arrays.asList(3, 12)).setName("Sh");
		LocationResult result =  execute(getLocations);
		assertTrue(result.getData().size() == 1);
										
	}
}
