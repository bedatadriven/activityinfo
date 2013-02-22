

package org.activityinfo.server.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.HashMap;
import java.util.Map;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
