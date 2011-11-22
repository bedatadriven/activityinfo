package org.sigmah.server.command;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.AddTarget;
import org.sigmah.shared.command.BatchCommand;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.TargetDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/schema1.db.xml")
public class TargetTest extends CommandTestCase {

	@Before
	public void setUser() {
		setUser(1);
	}

    @Test
    public void testTarget() throws CommandException {

        /*
           * Initial data load
           */

        SchemaDTO schema = execute(new GetSchema());

        UserDatabaseDTO db = schema.getDatabaseById(1);
        
        Date date1 = new Date();
        Date date2 = new Date();
        /*
           * Create a new Target
           */
      
        TargetDTO target = new TargetDTO();
        target.setName("Target0071");
        target.setDate1(date1);
        target.setDate2(date2);
        
        CreateResult cresult = execute(new AddTarget(db.getId(), target));

        int newId = cresult.getNewId();

        /*
           * Reload schema to verify the changes have stuck
           */

        schema = execute(new GetSchema());

        target = schema.getTargetById(newId);

        Assert.assertEquals("name", "Target0071", target.getName());
    }

    @Test
    public void updateTargetNameTest() throws Throwable {

        /* Update Sort Order */
        Map<String, Object> changes1 = new HashMap<String, Object>();
        changes1.put("name", "newNameOfTarget");

        execute(new BatchCommand(
                new UpdateEntity("Target", 1, changes1)
        ));

        /* Confirm the order is changed */

        SchemaDTO schema = execute(new GetSchema());
        Assert.assertEquals("newNameOfTarget", schema.getDatabaseById(1).getTargets().get(0).getName());
    }
}

