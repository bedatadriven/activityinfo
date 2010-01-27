package org.activityinfo.server.command;

import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

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

    @Test
    public void createWithSpecificCountry() throws CommandException {

        populate("multicountry");

        UserDatabaseDTO db = new UserDatabaseDTO();
        db.setName("Warchild Haiti");
        db.setFullName("Warchild Haiti");

        setUser(1);

        CreateEntity cmd = new CreateEntity(db);
        cmd.getProperties().put("countryId", 2);
        CreateResult cr = execute(cmd);

        Schema schema = execute(new GetSchema());

        UserDatabaseDTO newdb = schema.getDatabaseById(cr.getNewId());

        assertThat(newdb.getCountry(), is(notNullValue()));
        assertThat(newdb.getCountry().getName(), is(equalTo("Haiti")));
    }

}
