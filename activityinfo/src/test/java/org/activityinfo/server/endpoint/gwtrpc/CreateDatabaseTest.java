package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.shared.command.CreateEntity;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(InjectionSupport.class)
public class CreateDatabaseTest extends CommandTestCase {

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void testCreate() throws CommandException {

        UserDatabaseDTO db = new UserDatabaseDTO();
        db.setName("RIMS");
        db.setFullName("Reintegration Management Information System");

        CreateResult cr = execute(new CreateEntity(db));

        Schema schema = execute(new GetSchema());

        UserDatabaseDTO newdb = schema.getDatabaseById(cr.getNewId());

        Assert.assertEquals(db.getName(), newdb.getName());
        Assert.assertEquals(db.getFullName(), newdb.getFullName());
        Assert.assertNotNull(newdb.getCountry());
        Assert.assertEquals("Alex", newdb.getOwnerName());
    }

    @Test
    @OnDataSet("/dbunit/multicountry.db.xml")
    public void createWithSpecificCountry() throws CommandException {

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
