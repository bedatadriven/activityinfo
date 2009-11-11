package org.activityinfo.server.command;

import junit.framework.Assert;
import org.activityinfo.shared.command.BatchCommand;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.LocationTypeModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ActivityTest extends CommandTestCase {



	@Test
	public void testActivity() throws CommandException {
		
		populate( "schema1" );
		
		setUser(1);
		
		/*
		 * Initial data load
		 */
		
		Schema schema = execute(new GetSchema());
		
		UserDatabaseDTO db = schema.getDatabaseById(1);
				
		/*
		 * Create a new activity
		 */
		
		LocationTypeModel locType = schema.getCountryById(1).getLocationTypes().get(0);
		
		ActivityModel act = new ActivityModel();
		act.setName("Warshing the dishes");
		act.setLocationTypeId(locType.getId());
		act.setReportingFrequency(ActivityModel.REPORT_MONTHLY);
	
		CreateResult cresult = execute(CreateEntity.Activity(db, act));
		
		int newId = cresult.getNewId();
		
		/*
		 * Reload schema to verify the changes have stuck
		 */
		
		schema = execute(new GetSchema());
		
		act = schema.getActivityById(newId);
		
		Assert.assertEquals("name", "Warshing the dishes", act.getName());
		Assert.assertEquals("locationType", locType.getName(), act.getLocationType().getName());
		Assert.assertEquals("reportingFrequency", ActivityModel.REPORT_MONTHLY, act.getReportingFrequency());
			
	}

    @Test
    public void updateSortOrderTest() throws Throwable {
        populate( "schema1" );

        setUser(1);

        /* Update Sort Order */
        Map<String,Object> changes1 = new HashMap<String, Object>();
        changes1.put("sortOrder", 2);
        Map<String,Object> changes2 = new HashMap<String, Object>();
        changes2.put("sortOrder", 1);

        execute(new BatchCommand(
                new UpdateEntity("Activity", 1, changes1),
                new UpdateEntity("Activity", 2, changes2)
        ));

        /* Confirm the order is changed */

        Schema schema = execute(new GetSchema());
        Assert.assertEquals(2, schema.getDatabaseById(1).getActivities().get(0).getId());
        Assert.assertEquals(1, schema.getDatabaseById(1).getActivities().get(1).getId());


    }
	
}
