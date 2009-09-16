package org.activityinfo.server.command;

import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Test;
import org.junit.Assert;

public class CreateDatabaseTest extends CommandTestCase {

    @Test
    public void testCreate() throws CommandException {

        populate("sites-simple1");

        UserDatabaseDTO db = new UserDatabaseDTO();
        db.setName("RIMS");
        db.setFullName("Reintegration Management Information System");

        setUser(1);

        CreateResult cr = execute(new CreateEntity(db));

        Schema schema = execute(new GetSchema());

        UserDatabaseDTO newdb = schema.getDatabaseById(cr.getNewId());

        Assert.assertEquals(db.getName(), newdb.getName());
        Assert.assertEquals(db.getFullName(), newdb.getFullName());
        Assert.assertNotNull(newdb.getCountry());
        Assert.assertEquals("Alex", newdb.getOwnerName());

                
    }

}
