/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.test.InjectionSupport;

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

        SchemaDTO schema = execute(new GetSchema());

        ActivityDTO activity = schema.getActivityById(1);
        AttributeGroupDTO group = activity.getAttributeGroupById(result.getNewId());

        Assert.assertNotNull("attribute group is created", group);
        Assert.assertEquals("name is correct", group.getName(), "Type de Conflit");
        Assert.assertTrue("multiple allowed is set to true", group.isMultipleAllowed());
    }


    @Test
    public void testUpdate() throws Exception {

        // initial data load

        SchemaDTO schema = execute(new GetSchema());

        // change the name of an entity group
        ActivityDTO activity = schema.getActivityById(1);
        AttributeGroupDTO group = activity.getAttributeGroups().get(0);
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
