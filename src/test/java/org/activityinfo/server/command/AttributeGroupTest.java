package org.activityinfo.server.command;

import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AttributeGroupModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.junit.Test;
import org.junit.Assert;

import java.util.Map;
import java.util.HashMap;
/*
 * @author Alex Bertram
 */

public class AttributeGroupTest extends CommandTestCase {


    @Test
    public void testUpdate() throws Exception {

        populate( "schema1" );

		setUser(1);

        // initial data load

		Schema schema = execute(new GetSchema());

        // change the name of an entity group

		ActivityModel activity = schema.getActivityById(1);
        AttributeGroupModel group = activity.getAttributeGroups().get(0);
        group.setName("Foobar");

        Map<String,Object> changes = new HashMap<String, Object>();
        changes.put("name", group.getName());

        execute(new UpdateEntity(group, changes));

        // reload data
        schema = execute(new GetSchema());

        // verify the property has been duly changed
        Assert.assertEquals(group.getName(), schema.getActivityById(1).getAttributeGroups().get(0).getName());
        
    }
}
