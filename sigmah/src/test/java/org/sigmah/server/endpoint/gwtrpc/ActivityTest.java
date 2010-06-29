/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import java.util.HashMap;
import java.util.Map;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class ActivityTest extends CommandTestCase {


    @Test
    public void testActivity() throws CommandException {

        /*
           * Initial data load
           */

        SchemaDTO schema = execute(new GetSchema());

        UserDatabaseDTO db = schema.getDatabaseById(1);

        /*
           * Create a new activity
           */

        LocationTypeDTO locType = schema.getCountryById(1).getLocationTypes().get(0);

        ActivityDTO act = new ActivityDTO();
        act.setName("Warshing the dishes");
        act.setLocationTypeId(locType.getId());
        act.setReportingFrequency(ActivityDTO.REPORT_MONTHLY);

        CreateResult cresult = execute(CreateEntity.Activity(db, act));

        int newId = cresult.getNewId();

        /*
           * Reload schema to verify the changes have stuck
           */

        schema = execute(new GetSchema());

        act = schema.getActivityById(newId);

        Assert.assertEquals("name", "Warshing the dishes", act.getName());
        Assert.assertEquals("locationType", locType.getName(), act.getLocationType().getName());
        Assert.assertEquals("reportingFrequency", ActivityDTO.REPORT_MONTHLY, act.getReportingFrequency());

    }

    @Test
    public void updateSortOrderTest() throws Throwable {

        /* Update Sort Order */
        Map<String, Object> changes1 = new HashMap<String, Object>();
        changes1.put("sortOrder", 2);
        Map<String, Object> changes2 = new HashMap<String, Object>();
        changes2.put("sortOrder", 1);

        execute(new BatchCommand(
                new UpdateEntity("Activity", 1, changes1),
                new UpdateEntity("Activity", 2, changes2)
        ));

        /* Confirm the order is changed */

        SchemaDTO schema = execute(new GetSchema());
        Assert.assertEquals(2, schema.getDatabaseById(1).getActivities().get(0).getId());
        Assert.assertEquals(1, schema.getDatabaseById(1).getActivities().get(1).getId());
    }

}
