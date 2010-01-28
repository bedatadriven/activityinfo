package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.AttributeGroupModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class AttributeGroupTest extends CommandTestCase {


    @Before
    public void setUp() {
        setUser(1);
    }

    @Test
    public void testCreate() throws Exception {

        // execute the command

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("name", "Type de Conflit");
        properties.put("multipleAllowed", true);
        properties.put("activityId", 1);

        CreateEntity cmd = new CreateEntity("AttributeGroup", properties);

        CreateResult result = execute(cmd);

        // check if it has been added

        Schema schema = execute(new GetSchema());

        ActivityModel activity = schema.getActivityById(1);
        AttributeGroupModel group = activity.getAttributeGroupById(result.getNewId());

        Assert.assertNotNull("attribute group is created", group);
        Assert.assertEquals("name is correct", group.getName(), "Type de Conflit");
        Assert.assertTrue("multiple allowed is set to true", group.isMultipleAllowed());
    }


    @Test
    public void testUpdate() throws Exception {

        // initial data load

        Schema schema = execute(new GetSchema());

        // change the name of an entity group
        ActivityModel activity = schema.getActivityById(1);
        AttributeGroupModel group = activity.getAttributeGroups().get(0);
        group.setName("Foobar");

        Map<String, Object> changes = new HashMap<String, Object>();
        changes.put("name", group.getName());

        execute(new UpdateEntity(group, changes));

        // reload data
        schema = execute(new GetSchema());

        // verify the property has been duly changed
        Assert.assertEquals(group.getName(), schema.getActivityById(1).getAttributeGroups().get(0).getName());

    }
}
