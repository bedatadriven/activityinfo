package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

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

        SchemaDTO schema = execute(new GetSchema());

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

        SchemaDTO schema = execute(new GetSchema());

        UserDatabaseDTO newdb = schema.getDatabaseById(cr.getNewId());

        assertThat(newdb.getCountry(), is(notNullValue()));
        assertThat(newdb.getCountry().getName(), is(equalTo("Haiti")));
    }

}
